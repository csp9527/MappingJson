package com.csp.json2json.engine;

import javax.script.ScriptException;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-12 9:30
 * @version: 1.0
 */
public interface JsonParserEngine {

    void parser(String json);

    Object getValue(String key) throws ScriptException, NoSuchMethodException;
}
