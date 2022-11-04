package com.csp.mappingjson.mapper;

import com.csp.mappingjson.coter.Converter;
import com.csp.mappingjson.coter.DefaultConverter;
import com.csp.mappingjson.engine.JsJsonParserEngine;
import com.csp.mappingjson.engine.JsonParserEngine;
import com.csp.mappingjson.model.JsonNode;
import com.csp.mappingjson.model.NodeType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: json转json映射
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 15:44
 * @version: 1.0
 */
public class JsonMapper {

    private Gson gson = new GsonBuilder().serializeNulls().create();
    // 转化器实例Map key:类限定名 value:类实例
    private final Map<String, Converter> converterMap = new HashMap<>(256);
    // 目标json定义
    private Map<String, List<JsonNode>> jsonDefinitionMap;

    // 数据来源地址前缀
    private static final String SOURCE_LOCATION_PREFIX = "#";

    // 数组占位符
    private static final String ARRAY_MARK = "[?]";
    private static final String PLACEHOLDER = "?";
    private static final String POINT = ".";
    private static final String ROOT_NODE_NAME = "root";
    // 默认转化器类限定名
    private static final String DEFAULT_CONVERTER_REFERENCE = DefaultConverter.class.getName();

    private JsonParserEngine jsonParserEngine;

    /**
     * 通过js解析引擎转换
     * @param definitionJson 目标json定义
     * @param sourceJson 来源json
     * @return 目标json
     */
    public String mappingByJsEngine(String definitionJson, String sourceJson) {
        Assert.hasText(definitionJson, "待转换jsonNodeDefinition不能为空");
        Assert.hasText(sourceJson, "待转换sourceJson不能为空");
        Map<String, List<JsonNode>> jsonDefinitionMap  = gson.fromJson(definitionJson, new TypeToken<Map<String, List<JsonNode>>>() {}.getType());
        JsonParserEngine jsonParserEngine = new JsJsonParserEngine();
        jsonParserEngine.parser(gson.toJson(sourceJson));
        return this.mapping(jsonDefinitionMap, jsonParserEngine);
    }

    /**
     * json映射
     * @param jsonDefinitionMap 目标json定义Map
     * @param jsonParserEngine 来源json解析引擎
     * @return 目标json
     */
    public String mapping(Map<String, List<JsonNode>> jsonDefinitionMap, JsonParserEngine jsonParserEngine) {
        Assert.notEmpty(jsonDefinitionMap, "JsonDefinition不能为空");
        Assert.notNull(jsonParserEngine, "Json解析引擎不能为空");
        this.jsonDefinitionMap = jsonDefinitionMap;
        this.jsonParserEngine = jsonParserEngine;
        Map jsonMap = doMapping(ROOT_NODE_NAME, "");
        return gson.toJson(jsonMap);
    }

    /**
     * json映射
     * @param nodeName 当前节点名称
     * @param parentSourceLocation 父数据来源定位
     * @return 目标map
     */
    private Map doMapping(String nodeName, String parentSourceLocation) {
        Map<String, Object> jsonMap = new HashMap<>();
        List<JsonNode> nodes = jsonDefinitionMap.get(nodeName);
        for (JsonNode node : nodes) {
            String name = node.getName();
            String currentSourceLocation = getCurrentSourceLocation(parentSourceLocation, node.getSourceLocation());

            Converter converter = generateConverter(node.getConverter());
            
            Object parsedVal;
            if (NodeType.isPrimitive(node.getType())) {
                parsedVal = jsonParserEngine.getValue(currentSourceLocation);
                parsedVal = converter.doConverter(parsedVal, node);
            } else if (NodeType.isArray(node.getType())) {
                List list = new ArrayList();
                Object innerParsedVal = jsonParserEngine.getValue(currentSourceLocation);
                // 如果真实类型为基本类型
                if (NodeType.isPrimitive(node.getActualType())) {
                    Object val;
                    if (innerParsedVal instanceof List) { // 解析值为数组
                        List innerList = (List) innerParsedVal;
                        if (innerList.size() > 0) {
                            for (Object inner : innerList) {
                                val = converter.doConverter(inner, node);
                                list.add(val);
                            }
                        } else {
                            val = null;
                            list.add(val);
                        }
                    } else { // 解析值为基本类型或者对象类型
                        val = converter.doConverter(innerParsedVal, node);
                        list.add(val);
                    }
                } else if (!NodeType.isArray(node.getActualType())) { // 如果真实类型为对象类型
                    Object val;
                    if (innerParsedVal instanceof List) {// 解析值为数组
                        List innerList = (List) innerParsedVal;
                        for (int i = 0; i < innerList.size() ; i++) {
                            String arrSourceLocation = currentSourceLocation + ARRAY_MARK.replace(PLACEHOLDER, String.valueOf(i));
                            val = this.doMapping(node.getActualType(), arrSourceLocation);
                            list.add(val);
                        }
                    } else { // 解析值为基本类型或者对象类型
                        val = this.doMapping(node.getActualType(), currentSourceLocation);
                        list.add(val);
                    }
                } else {
                    // 数组类型不转换
                }
                parsedVal = list;
            } else {
                parsedVal = doMapping(node.getActualType(), currentSourceLocation);
            }
            // 如果为空使用默认值
            if (parsedVal == null) {
                parsedVal = node.getValue();
            }
            jsonMap.put(name, parsedVal);
        }
        return jsonMap;
    }

    /**
     * 创建数据转化器
     * @param converterReference 数据转换器限定名
     * @return 数据转换器实例
     */
    private Converter generateConverter(String converterReference) {
        if (StringUtils.isBlank(converterReference)) {
            converterReference = DEFAULT_CONVERTER_REFERENCE;
        }
        try {
            if (!this.converterMap.containsKey(converterReference)) {
                Class conClazz = Class.forName(converterReference);
                Converter converter = (Converter) conClazz.newInstance();
                this.converterMap.put(converterReference, converter);
            }
            return this.converterMap.get(converterReference);
        } catch (Exception e) {
            throw new RuntimeException("转化器:" + converterReference + " 创建失败", e);
        }
    }

    /**
     * 获取当前数据来源定位
     * @param parentSourceLocation
     * @param sourceLocation
     * @return
     */
    private String getCurrentSourceLocation(String parentSourceLocation, String sourceLocation) {
        if (StringUtils.isBlank(sourceLocation)) {
            return sourceLocation;
        }
        if (sourceLocation.startsWith(SOURCE_LOCATION_PREFIX)) {
            return sourceLocation.substring(1);
        }
        if (StringUtils.isBlank(parentSourceLocation)) {
            return sourceLocation;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parentSourceLocation).append(POINT).append(sourceLocation);
        return sb.toString();
    }
}
