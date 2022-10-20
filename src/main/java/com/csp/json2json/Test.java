package com.csp.json2json;

import com.csp.json2class.dto.ArrDto;
import com.csp.json2class.dto.SonDto;
import com.csp.json2class.dto.TestDto;
import com.csp.json2json.model.Node;
import com.google.gson.*;

import java.util.*;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-12 20:14
 * @version: 1.0
 */
public class Test {

    private static Gson gson = new GsonBuilder().serializeNulls().create();

    public static final String POINT = ".";

    private static Map<String, List<Node>> jsonDefinitionMap = new LinkedHashMap<>();

    public static void main(String[] args) {

        String json = "{\n" +
                "    \"arrDtos\": [\n" +
                "        {\n" +
                "            \"code\": \"0\",\n" +
                "            \"isTrue\": true,\n" +
                "            \"hight\": 175\n" +
                "        },\n" +
                "        {\n" +
                "            \"code\": \"1\",\n" +
                "            \"isTrue\": false,\n" +
                "            \"hight\": 176\n" +
                "        }\n" +
                "    ],\n" +
                "    \"name\": \"haha\",\n" +
                "    \"sonDto\": {\n" +
                "    \t\"hobby\": \"read\",\n" +
                "        \"age\": 18,\n" +
                "        \"canDrive\": true,\n" +
                "        \"address\": [123,456],\n" +
                "        \"sonDto\": {\n" +
                "        \t\"name\": \"hehe\"\n" +
                "        }\n" +
                "    },\n" +
                "    account:[\"123\", \"456\"]\n" +
                "}";

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        parse(jsonObject, "", "root");

        System.out.println(gson.toJson(jsonDefinitionMap));
    }

    public static void parse(JsonObject jsonObject, String parentName, String currentName) {
        List<Node> nodes = new ArrayList<>();
        currentName = getCurrentName(parentName, currentName);
        jsonDefinitionMap.put(currentName, nodes);

        Set<String> set = jsonObject.keySet();
        for (String key : set) {

            String actualType = getCurrentName(currentName, key);
            JsonElement val = jsonObject.get(key);
            Node node = new Node();
            node.setName(key);
            if (val instanceof JsonObject) {
                node.setType("object");
                node.setActualType(actualType);
                parse((JsonObject) val, currentName, key);
            } else if (val instanceof JsonArray) {
                node.setType("array");
                JsonElement innerVal = ((JsonArray) val).get(0);
                if (innerVal instanceof JsonObject) {
                    node.setActualType(actualType);
                    parse((JsonObject) innerVal, currentName, key);
                } else if (innerVal instanceof JsonPrimitive) {
                    JsonPrimitive jsonPrimitive = (JsonPrimitive) innerVal;
                    if (jsonPrimitive.isString()) {
                        node.setActualType("string");
                        node.setValue(jsonPrimitive.getAsString());
                    } else if (jsonPrimitive.isNumber()) {
                        node.setActualType("double");
                        node.setValue(jsonPrimitive.getAsDouble());
                    } else if (jsonPrimitive.isBoolean()) {
                        node.setActualType("boolean");
                        node.setValue(jsonPrimitive.getAsBoolean());
                    }
                }
            } else if (val instanceof JsonPrimitive) {
                JsonPrimitive jsonPrimitive = (JsonPrimitive) val;
                if (jsonPrimitive.isString()) {
                    node.setType("string");
                    node.setValue(jsonPrimitive.getAsString());
                } else if (jsonPrimitive.isNumber()) {
                    node.setType("double");
                    node.setValue(jsonPrimitive.getAsDouble());
                } else if (jsonPrimitive.isBoolean()) {
                    node.setType("boolean");
                    node.setValue(jsonPrimitive.getAsBoolean());
                }

            } else if (val instanceof JsonNull) {
                node.setType("string");
                node.setValue(null);
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
