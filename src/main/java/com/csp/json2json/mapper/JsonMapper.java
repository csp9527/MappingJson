package com.csp.json2json.mapper;

import com.csp.json2json.StringUtils;
import com.csp.json2json.TypeCheck;
import com.csp.json2json.coter.Converter;
import com.csp.json2json.engine.JsonParserEngine;
import com.csp.json2json.model.Node;
import com.google.gson.Gson;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptException;
import java.util.*;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-12 13:38
 * @version: 1.0
 */
public class JsonMapper {

    private Gson gson = new Gson();

    private final Map<String, Converter> converterMap = new HashMap<>(256);
    private Map<String, List<Node>> jsonDefinitionMap = new LinkedHashMap<>();

    private static final String TARGET_LOCATION_PREFIX = "#";

    private static final String ARRAY_MARK = "[?]";
    private static final String PLACEHOLDER = "?";
    public static final String POINT = ".";
    private static final String ROOT_NODE_NAME = "root";
    private static final String DEFAULT_CONVERTER_REFERENCE = "com.csp.json2json.coter.DefaultConverter";

    private JsonParserEngine jsonParserEngine;

    public String doMapping(Map<String, List<Node>> jsonDefinitionMap, JsonParserEngine jsonParserEngine)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, ScriptException, NoSuchMethodException {
        this.jsonDefinitionMap = jsonDefinitionMap;
        this.jsonParserEngine = jsonParserEngine;
        Map jsonMap = doMapping(ROOT_NODE_NAME, "");
        return gson.toJson(jsonMap);
    }

    private Map doMapping(String nodeName, String parentTargetLocation)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, ScriptException, NoSuchMethodException {
        Map<String, Object> jsonMap = new HashMap<>();
        List<Node> nodes = jsonDefinitionMap.get(nodeName);
        for (Node node : nodes) {
            String name = node.getName();
            String currentTargetLocation = getCurrentTargetLocation(parentTargetLocation, node.getTargetLocation());
            if (StringUtils.isBlank(currentTargetLocation)) {
                jsonMap.put(name, node.getValue());
                continue;
            }
            Converter converter = generateConverter(node.getConverter());

            Object parsedVal;
            if (TypeCheck.isPrimitive(node.getType())) {
                parsedVal = jsonParserEngine.getValue(currentTargetLocation);
            } else if (TypeCheck.isArray(node.getType())) {
                List list = new ArrayList();
                Object[] objs = null;
                Object innerParsedVal = jsonParserEngine.getValue(currentTargetLocation);
                if (((ScriptObjectMirror) innerParsedVal).isArray()) {
                    objs = ((ScriptObjectMirror) innerParsedVal).values().toArray();
                }
                for (int i = 0; i < objs.length ; i++) {
                    currentTargetLocation = currentTargetLocation + ARRAY_MARK.replace(PLACEHOLDER, String.valueOf(i));
                    Object val = this.doMapping(node.getActualType(), currentTargetLocation);
                    list.add(val);
                }
                parsedVal = list;
            } else {
                parsedVal = doMapping(node.getActualType(), currentTargetLocation);
            }
            parsedVal = converter.doConverter(parsedVal, null);
            jsonMap.put(name, parsedVal);

        }

        return jsonMap;
    }

    private Converter generateConverter(String converterReference) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (StringUtils.isBlank(converterReference)) {
            converterReference = DEFAULT_CONVERTER_REFERENCE;
        }
        if (!this.converterMap.containsKey(converterReference)) {
            Class conClazz = Class.forName(converterReference);
            Converter converter = (Converter) conClazz.newInstance();
            this.converterMap.put(converterReference, converter);
        }

        return this.converterMap.get(converterReference);
    }

    private String getCurrentTargetLocation(String parentTargetLocation, String targetLocation) {
        if (StringUtils.isBlank(targetLocation)) {
            return targetLocation;
        }
        if (targetLocation.startsWith(TARGET_LOCATION_PREFIX)) {
            return targetLocation.substring(1);
        }
        if (StringUtils.isBlank(parentTargetLocation)) {
            return targetLocation;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parentTargetLocation).append(POINT).append(targetLocation);
        return sb.toString();
    }
}
