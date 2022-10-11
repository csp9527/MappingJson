package com.csp.engine;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-10 11:07
 * @version: 1.0
 */
public class JsonParserEngine {

    private static final String ENGINE_NAME = "js";
    private static final String FUNC_NAME = "parser";

    private static final String FUNC = "function parser(json, exr) {\n" +
            "var obj = eval('(' + json + ')');\n" +
            "return eval('obj.' + exr);\n" +
            "}";

    public static Object parser(String json, String targetLocation) throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(ENGINE_NAME);
        engine.eval(FUNC);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;
            Object val = invoke.invokeFunction(FUNC_NAME, json, targetLocation);
            return val;
        }
        return null;
    }
}
