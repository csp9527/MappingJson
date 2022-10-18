package com.csp.json2json;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-12 14:49
 * @version: 1.0
 */
public class Assert {
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
