package com.csp.json2json.coter;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-09 16:38
 * @version: 1.0
 */
public interface Converter {

    Object doConverter(Object originVal, String mapping);
}
