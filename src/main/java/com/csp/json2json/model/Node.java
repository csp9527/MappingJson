package com.csp.json2json.model;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-12 9:43
 * @version: 1.0
 */
public class Node {

    // 属性名
    private String name = "";
    // 默认值
    private Object value;
    // 类型
    private String type = "";
    // 转换器(类限定名)
    private String converter = "";
    // 映射属性定位
    private String targetLocation = "";

    // 当type为Array时，对应的实际类型
    private String actualType = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public String getActualType() {
        return actualType;
    }

    public void setActualType(String actualType) {
        this.actualType = actualType;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", type='" + type + '\'' +
                ", converter='" + converter + '\'' +
                ", targetLocation='" + targetLocation + '\'' +
                ", actualType='" + actualType + '\'' +
                '}';
    }
}
