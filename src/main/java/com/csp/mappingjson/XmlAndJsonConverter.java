package com.csp.mappingjson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.tree.BaseElement;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description: xml格式与json格式转换工具类
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-19 14:37
 * @version: 1.0
 */
public class XmlAndJsonConverter {

    private static final String ATTR_PREFIX = "_";
    private static final String DEFAULT_KEY_PREFIX = "$";
    private static final String DEFAULT_KEY = "$key";

    private static final String DEFAULT_XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";

    private static final String ROOT_ELE_NAME = "root";

    private static final String POINT = ".";

    /**
     * xml->json
     * @param xml 待转换xml数据
     * @param eleNameOfArrTypeList xml文件中需要转换为数组类型的节点名称集合
     * @return 转换后json字符串
     */
    public static String xml2json(String xml, List<String> eleNameOfArrTypeList) {
        Assert.hasText(xml, "xml参数不能为空或空字符串");
        if (eleNameOfArrTypeList == null) {
            eleNameOfArrTypeList = new ArrayList<>();
        }
        try {
            JsonObject jsonObject = new JsonObject();
            Document document = DocumentHelper.parseText(xml);
            jsonObject = doXml2json(document.getRootElement(), jsonObject, "", eleNameOfArrTypeList);
            return jsonObject.toString();
        } catch (DocumentException e) {
            throw new RuntimeException("xml解析异常", e);
        } catch (Exception e) {
            throw new RuntimeException("xml转json异常", e);
        }
    }

    /**
     * xml->json
     * @param currentElement xml当前待转换节点
     * @param jsonObject json当前待赋属性的对象
     * @param parentPath 父元素路径
     * @param eleNameOfArrTypeList xml文件中需要转换为数组类型的节点名称集合
     * @return json赋属性后的对象
     */
    private static JsonObject doXml2json(Element currentElement, JsonObject jsonObject, String parentPath, List<String> eleNameOfArrTypeList) {

        String name = currentElement.getName();
        String currentPath = getCurrentPath(parentPath, name);
        String nodeValue = currentElement.getTextTrim();
        // 获取当前节点的子节点与属性
        List<Element> elementList = currentElement.elements();
        List<Attribute> attributeList = currentElement.attributes();

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
                if (StringUtils.isNotBlank(nodeValue)) {
                    js.addProperty(DEFAULT_KEY, nodeValue);
                }
                jsonObject.add(name, js);
            }
        } else {
            JsonObject js = new JsonObject();
            if (jsonObject.has(name)) { // 判断json中是否存在同名的属性
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
            } else if (eleNameOfArrTypeList.contains(currentPath)) { // 判断json当前属性是否按照数组类型创建
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(js);
                jsonObject.add(name, jsonArray);
            } else {
                jsonObject.add(name, js);
            }
            if (StringUtils.isNotBlank(nodeValue)) {
                js.addProperty(DEFAULT_KEY, nodeValue);
            }
            // 遍历所有节点属性
            for (Attribute attribute : attributeList) {
                String attributeName = attribute.getName();
                String attrrbuteValue = attribute.getValue();
                // 属性名使用"_"做前缀
                js.addProperty(ATTR_PREFIX + attributeName, attrrbuteValue);
            }
            // 遍历转换所有子节点
            for (Element element : elementList) {
                doXml2json(element, js, currentPath, eleNameOfArrTypeList);
            }
        }
        return jsonObject;
    }

    /**
     * 获取当前元素路径
     * @param parentPath
     * @param name
     * @return
     */
    private static String getCurrentPath(String parentPath, String name) {
        if (StringUtils.isBlank(parentPath)) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parentPath).append(POINT).append(name);
        return sb.toString();
    }

    /**
     * json->xml
     * @param json 待解析的json字符串参数
     * @return 转换后xml字符串
     */
    public static String json2xml(String json) {
        return json2xml(json, null);
    }

    /**
     * json->xml
     * @param json 待解析的json字符串参数
     * @param xmlHeader xml文件头
     * @return 转换后xml字符串
     */
    public static String json2xml(String json, String xmlHeader) {
        return json2xml(json, xmlHeader, null);
    }

    /**
     * json->xml
     * @param json 待解析的json字符串参数
     * @param xmlHeader xml文件头
     * @param rootName 根节点名称
     * @return 转换后xml字符串
     */
    public static String json2xml(String json, String xmlHeader, String rootName) {
        Assert.hasText(json, "json参数不能为空或空字符串");
        if (StringUtils.isBlank(xmlHeader)) {
            xmlHeader = DEFAULT_XML_HEADER;
        }
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        Set<String> keys = jsonObject.keySet();
        Assert.notEmpty(keys, "json对象至少要有一个属性");
        if (keys.size() == 1) {
            Element rootEle = null;
            if (StringUtils.isNotBlank(rootName)) {
                rootEle = new BaseElement(rootName);
            }
            String key = keys.iterator().next();
            Element resultEle = doJson2xml(jsonObject.get(key), rootEle, key);
            return xmlHeader + resultEle.asXML();
        } else {
            Element rootEle;
            if (StringUtils.isBlank(rootName)) {
                rootName = ROOT_ELE_NAME;
            }
            rootEle = new BaseElement(rootName);
            for (String key : keys) {
                doJson2xml(jsonObject.get(key), rootEle, key);

            }
            return xmlHeader + rootEle.asXML();
        }
    }

    /**
     * json->xml
     * @param jsonElement json当前节点
     * @param parentXmlElement xml父节点
     * @param name xml父节点名称
     * @return xml增加子节点后的对象
     */
    private static Element doJson2xml(JsonElement jsonElement, Element parentXmlElement, String name) {
        if (jsonElement instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) jsonElement;
            for (JsonElement inner : jsonArray) {
                doJson2xml(inner, parentXmlElement, name);
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
                doJson2xml(sonJsonObject.get(key), currentElement, key);
            }
        } else {
            if (name.startsWith(ATTR_PREFIX)) { // 如果json属性名以"_"开头，则增加为节点属性
                if (parentXmlElement == null) {
                    parentXmlElement = new BaseElement(name);
                }
                parentXmlElement.addAttribute(name.substring(1), jsonElement.getAsString());
            } else if (name.startsWith(DEFAULT_KEY_PREFIX)) { // 如果json属性名以"$"开头，则增加为节点值
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
