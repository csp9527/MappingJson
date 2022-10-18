package com.csp.json2json;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-12 14:44
 * @version: 1.0
 */
public class TypeCheck {

    public static final String STRING = "string";
    public static final String DOUBLE = "double";
    public static final String INT = "int";
    public static final String BOOLEAN = "boolean";
    public static final String ARRAY = "array";
    public static final String OBJECT = "object";

    public static boolean isPrimitive(String type) {
        Assert.notNull(type, "属性类型不能为空");
        if (OBJECT.equals(type) || ARRAY.equals(type)) {
            return false;
        }
        return true;
    }

    public static boolean isArray(String type) {
        Assert.notNull(type, "属性类型不能为空");
        if (ARRAY.equals(type)) {
            return true;
        }
        return false;
    }
}
