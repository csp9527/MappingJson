package com.csp.mappingjson.model;

import org.springframework.util.Assert;

/**
 * @description: json节点类型
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 14:01
 * @version: 1.0
 */
public class NodeType {
    public static final String STRING = "STRING";
    public static final String DOUBLE = "DOUBLE";
    public static final String INTEGER = "INTEGER";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String ARRAY = "ARRAY";
    public static final String OBJECT = "OBJECT";

    public static boolean isPrimitive(String type) {
        Assert.notNull(type, "属性类型不能为空");
        if (STRING.equals(type) || DOUBLE.equals(type) || INTEGER.equals(type) || BOOLEAN.equals(type)) {
            return true;
        }
        return false;
    }

    public static boolean isArray(String type) {
        Assert.notNull(type, "属性类型不能为空");
        if (ARRAY.equals(type)) {
            return true;
        }
        return false;
    }
}
