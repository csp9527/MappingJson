package com.csp.json2json;

import com.google.gson.*;
import org.dom4j.*;
import org.dom4j.tree.BaseElement;

import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-17 14:44
 * @version: 1.0
 */
public class Xml2JsonTest {

    private static String arrList = "rd";

    private static final String ATTR_PREFIX = "-";

    public static void main(String[] args) throws DocumentException {
        /*String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root code=\"1\">\n" +
                "\t<rds>\n" +
                "\t\t<rd id=\"123\">\n" +
                "\t\t\t<name>haha</name>\n" +
                "\t\t\t<age>20</age>\n" +
                "\t\t</rd>\n" +
                "\t\t<rd id=\"456\">\n" +
                "\t\t\t<name>hehe</name>\n" +
                "\t\t\t<age>25</age>\n" +
                "\t\t</rd>\n" +
                "\t</rds>\n" +
                "</root>";

        System.out.println(toJson(xml));*/

        String json = "{\n" +
                "    \"root\": {\n" +
                "        \"-code\": \"1\",\n" +
                "        \"rds\": {\n" +
                "            \"rd\": [\n" +
                "                {\n" +
                "                    \"-id\": \"123\",\n" +
                "                    \"age\": \"20\",\n" +
                "                    \"name\": \"haha\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"-id\": \"456\",\n" +
                "                    \"age\": \"25\",\n" +
                "                    \"name\": \"hehe\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}";

        System.out.println(toXml(json));
    }

    public static String toJson(String xml) throws DocumentException {
        JsonObject jsonObject = new JsonObject();
        Document document = DocumentHelper.parseText(xml);

        jsonObject = xml2json(document.getRootElement(), jsonObject);
        return jsonObject.toString();
    }

    private static JsonObject xml2json(Element node, JsonObject jsonObject) {

        List<Element> elementList = node.elements();

        String name = node.getName();
        List<Attribute> attributeList = node.attributes();

        if (elementList.isEmpty() && attributeList.isEmpty()) {
            String nodeValue = node.getTextTrim();
            jsonObject.addProperty(name, nodeValue);
        } else {
            JsonObject js = new JsonObject();
            // 判断json中是否存在同名的key
            if (jsonObject.has(name)) {
                JsonArray jsonArray = null;
                Object o = jsonObject.get(name);
                if (o instanceof JsonArray) {
                    jsonArray = (JsonArray) o;
                } else {
                    jsonArray = new JsonArray();
                    jsonArray.add((JsonObject)o);
                }
                jsonArray.add(js);
                jsonObject.add(name, jsonArray);
            } else if (arrList.contains(name)) {
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(js);
                jsonObject.add(name, jsonArray);
            } else {
                jsonObject.add(name, js);
            }
            for (Attribute attribute : attributeList) {
                String attributeName = attribute.getName();
                String attrrbuteValue = attribute.getValue();
                js.addProperty(ATTR_PREFIX + attributeName, attrrbuteValue);
            }
            for (Element element : elementList) {
                xml2json(element, js);
            }
        }
        return jsonObject;
    }

    public static String toXml(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Set<String> set = jsonObject.keySet();
        if (set.size() == 1) {
            for (String key : set) {
                Element root = json2xml(jsonObject.get(key), null, key);
                return root.asXML();
            }
        }
        return null;
    }

    private static Element json2xml(JsonElement jsonElement, Element parentXmlElement, String name) {
        if (jsonElement instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) jsonElement;
            for (JsonElement inner : jsonArray) {
                json2xml(inner, parentXmlElement, name);
            }
        } else if (jsonElement instanceof JsonObject) {
            JsonObject sonJsonObject = (JsonObject) jsonElement;
            Element currentElement;
            if (parentXmlElement == null) {
                currentElement = new BaseElement(name);
                parentXmlElement = currentElement;
            } else {
                currentElement = parentXmlElement.addElement(name);
            }
            Set<String> set = sonJsonObject.keySet();
            for (String key : set) {
                json2xml(sonJsonObject.get(key), currentElement, key);
            }
        } else {
            if (name.startsWith(ATTR_PREFIX)) {
                parentXmlElement.addAttribute(name.substring(1), jsonElement.getAsString());
            } else {
                Element el = parentXmlElement.addElement(name);
                el.addText(jsonElement.getAsString());
            }
        }

        return parentXmlElement;
    }
}
