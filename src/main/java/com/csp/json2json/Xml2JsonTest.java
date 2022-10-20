package com.csp.json2json;

import com.google.gson.*;
import org.dom4j.*;
import org.dom4j.tree.BaseElement;

import java.util.List;
import java.util.Map;
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

    private static final String ATTR_PREFIX = "_";
    private static final String DEFAULT_KEY_PREFIX = "$";
    private static final String DEFAULT_KEY = "$key";

    public static void main(String[] args) throws DocumentException {
        String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root>\n" +
                "</root>";

        String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root code=\"1\">\n" +
                "</root>";

        String xml3 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root>\n" +
                "123456\n" +
                "</root>";

        String xml4 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root code=\"1\">\n" +
                "123456\n" +
                "</root>";

        String xml5 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root code=\"1\">\n" +
                "\t<rds>\n" +
                "\t\t<rd id=\"123\">\n" +
                "\t\t\t<age>20</age>\n" +
                "\t\t\t<name>haha</name>\n" +
                "\t\t</rd>\n" +
                "\t\t<rd id=\"456\">\n" +
                "\t\t\t<age>25</age>\n" +
                "\t\t\t<name>hehe</name>\n" +
                "\t\t</rd>\n" +
                "\t</rds>\n" +
                "</root>";

        String xml6 = "<root code=\"1\">\n" +
                "\t<rds>\n" +
                "\t\t<rd id=\"123\">\n" +
                "\t\t\t20\n" +
                "\t\t</rd>\n" +
                "\t\t<rd id=\"456\">\n" +
                "\t\t\t<age>25</age>\n" +
                "\t\t\t<name>hehe</name>\n" +
                "\t\t</rd>\n" +
                "\t</rds>\n" +
                "</root>";
        String xml7 = "<root code=\"1\">\n" +
                "\thaha\n" +
                "\t<rds>\n" +
                "\t\t<rd id=\"123\">\n" +
                "\t\t\t<age>20</age>\n" +
                "\t\t\t<name>haha</name>\n" +
                "\t\t</rd>\n" +
                "\t\t<rd id=\"456\">\n" +
                "\t\t\t<age>25</age>\n" +
                "\t\t\t<name>hehe</name>\n" +
                "\t\t</rd>\n" +
                "\t</rds>\n" +
                "</root>";

        String xml8 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root code=\"1\">\n" +
                "\thaha\n" +
                "\t<rds>\n" +
                "\t\t<rd id=\"123\">\n" +
                "\t\t\t20\n" +
                "\t\t</rd>\n" +
                "\t\t<rd id=\"456\">\n" +
                "\t\t\t<age>25</age>\n" +
                "\t\t\t<name>hehe</name>\n" +
                "\t\t</rd>\n" +
                "\t</rds>\n" +
                "</root>";

        String xml9 = "<root code=\"1\">\n" +
                "\thaha\n" +
                "\t<rds>\n" +
                "\t\t<rd id=\"123\">\n" +
                "\t\t\t20\n" +
                "\t\t</rd>\n" +
                "\t\t<rd id=\"456\">\n" +
                "\t\t\t<age>25</age>\n" +
                "\t\t\t<name>hehe</name>\n" +
                "\t\t</rd>\n" +
                "\t</rds>\n" +
                "\thehe\n" +
                "</root>";


        //String json = toJson(xml9);
        String json = "{1:2,2:4}";

        System.out.println(json);
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
        String nodeValue = node.getTextTrim();
        List<Attribute> attributeList = node.attributes();

        if (elementList.isEmpty()) {
            if (attributeList.isEmpty()) {
                jsonObject.addProperty(name, nodeValue);
            } else {
                JsonObject js = new JsonObject();
                for (Attribute attribute : attributeList) {
                    String attributeName = attribute.getName();
                    String attrrbuteValue = attribute.getValue();
                    js.addProperty(ATTR_PREFIX + attributeName, attrrbuteValue);
                }
                if (!StringUtils.isBlank(nodeValue)) {
                    js.addProperty(DEFAULT_KEY, nodeValue);
                }
                jsonObject.add(name, js);
            }
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
            if (!StringUtils.isBlank(nodeValue)) {
                js.addProperty(DEFAULT_KEY, nodeValue);
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
                if (parentXmlElement == null) {
                    parentXmlElement = new BaseElement(name);
                }
                parentXmlElement.addAttribute(name.substring(1), jsonElement.getAsString());
            } else if (name.startsWith(DEFAULT_KEY_PREFIX)) {
                if (parentXmlElement == null) {
                    parentXmlElement = new BaseElement(name);
                }
                parentXmlElement.addText(jsonElement.getAsString());
            } else {
                Element currentElement;
                if (parentXmlElement == null) {
                    currentElement = new BaseElement(name);
                    parentXmlElement = currentElement;
                } else {
                    currentElement = parentXmlElement.addElement(name);
                }
                currentElement.addText(jsonElement.getAsString());
            }
        }

        return parentXmlElement;
    }
}
