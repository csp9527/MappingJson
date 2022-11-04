package com.csp.mappingjson.engine;

/**
 * @description: json解析接口
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 14:47
 * @version: 1.0
 */
public interface JsonParserEngine {

    void parser(String json);

    Object getValue(String key);
}
