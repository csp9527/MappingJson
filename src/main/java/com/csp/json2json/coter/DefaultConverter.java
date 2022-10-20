package com.csp.json2json.coter;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-10 16:48
 * @version: 1.0
 */
public class DefaultConverter implements Converter {
    @Override
    public Object doConverter(Object originVal, String mapping) {
        return originVal;
    }
}
