package com.csp.mappingjson;

import com.csp.mappingjson.model.JsonNode;
import com.csp.mappingjson.model.NodeType;
import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @description: 根据json获取jsonDefiniton工具类
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 14:04
 * @version: 1.0
 */
public class JsonDefinitionUtil {

    // 默认根节点
    private static final String ROOT = "root";
    public static final String POINT = ".";

    /**
     * 根据json获取jsonDefiniton
     * @param json 带解析json
     * @return 解析后结果
     */
    public static String parse(String json) {
        Assert.hasText(json, "带解析json不能为空");
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        Map<String, List<JsonNode>> jsonDefinitionMap = new LinkedHashMap<>();

        doParse(jsonObject, "", ROOT, jsonDefinitionMap);

        return new GsonBuilder().serializeNulls().create().toJson(jsonDefinitionMap);
    }


    /**
     * 实际解析方法 （double,integer类型默认转为double;null默认转为string）
     * @param jsonObject 待解析json对象
     * @param parentName 父节点路径名称
     * @param currentName 当前节点名称
     * @param jsonDefinitionMap 存储解析结果
     */
    private static void doParse(JsonObject jsonObject, String parentName, String currentName, Map<String, List<JsonNode>> jsonDefinitionMap) {
        List<JsonNode> nodes = new ArrayList<>();
        currentName = getCurrentName(parentName, currentName);
        jsonDefinitionMap.put(currentName, nodes);

        Set<String> set = jsonObject.keySet();
        for (String key : set) {
            String actualType = getCurrentName(currentName, key);
            JsonElement val = jsonObject.get(key);
            JsonNode node = new JsonNode();
            node.setName(key);
            if (val instanceof JsonObject) {
                node.setType(NodeType.OBJECT);
                node.setActualType(actualType);
                doParse((JsonObject) val, currentName, key, jsonDefinitionMap);
            } else if (val instanceof JsonArray) {
                node.setType(NodeType.ARRAY);
                JsonElement innerVal = ((JsonArray) val).get(0);
                if (innerVal instanceof JsonObject) {
                    node.setActualType(actualType);
                    doParse((JsonObject) innerVal, currentName, key, jsonDefinitionMap);
                } else if (innerVal instanceof JsonPrimitive) {
                    JsonPrimitive jsonPrimitive = (JsonPrimitive) innerVal;
                    if (jsonPrimitive.isString()) {
                        node.setActualType(NodeType.STRING);
                    } else if (jsonPrimitive.isNumber()) {
                        node.setActualType(NodeType.DOUBLE);
                    } else if (jsonPrimitive.isBoolean()) {
                        node.setActualType(NodeType.BOOLEAN);
                    }
                } else {
                    // 数组类型不转换
                }
            } else if (val instanceof JsonPrimitive) {
                JsonPrimitive jsonPrimitive = (JsonPrimitive) val;
                if (jsonPrimitive.isString()) {
                    node.setType(NodeType.STRING);
                } else if (jsonPrimitive.isNumber()) {
                    node.setType(NodeType.DOUBLE);
                } else if (jsonPrimitive.isBoolean()) {
                    node.setType(NodeType.BOOLEAN);
                }

            } else if (val instanceof JsonNull) {
                node.setType(NodeType.STRING);
            }
            nodes.add(node);
        }
    }

    private static String getCurrentName(String parentName, String currentName) {
        if (StringUtils.isBlank(parentName)) {
            return currentName;
        }
        return parentName + POINT + currentName;
    }
}
