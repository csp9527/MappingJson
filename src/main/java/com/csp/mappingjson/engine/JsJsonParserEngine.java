package com.csp.mappingjson.engine;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: json解析 by js
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 14:50
 * @version: 1.0
 */
@Slf4j
public class JsJsonParserEngine implements JsonParserEngine {

    private static final String ENGINE_NAME = "js";
    private static final String FUNC_NAME = "parser";

    private static final String FUNC = "function parser(json, exr) {\n" +
            "var obj = eval('(' + json + ')');\n" +
            "return eval('obj.' + exr);\n" +
            "}";

    private String json;

    private static Invocable invoke;

    static {
        try {
            if (invoke == null) {
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName(ENGINE_NAME);
                engine.eval(FUNC);
                invoke = (Invocable) engine;
            }
        } catch (Exception e) {
            throw new RuntimeException("FUNC方法解析异常", e);
        }
    }

    @Override
    public void parser(String json) {
        this.json = json;
    }

    @Override
    public Object getValue(String key) {
        try {
            if (StringUtils.isBlank(key)) {
                return null;
            }
            Object val = invoke.invokeFunction(FUNC_NAME, json, key);
            return convert(val);
        } catch (Exception e) {
            log.error("Json解析引擎获取值失败", e);
            return null;
        }
    }

    private Object convert(Object original) {
        if (original == null) {
            return null;
        } else if (original instanceof String || original instanceof Integer || original instanceof Long
                || original instanceof Boolean || original instanceof Double) {
            return original;
        } else if (original instanceof ScriptObjectMirror) {
            ScriptObjectMirror jsOriginal = (ScriptObjectMirror)original;
            if (jsOriginal.isArray()) {
                List<Object> listResult = new ArrayList<>();
                int length = Integer.valueOf(jsOriginal.get("length").toString());
                for (int i = 0; i < length; i++) {
                    listResult.add(convert(jsOriginal.get("" + i)));
                }
                return listResult;
            } else if (jsOriginal.isFunction()) {
                // 不转换
                return null;
            }
            Map<String,Object> mapResult = new LinkedHashMap<>();
            for (Map.Entry<String,Object> entry: jsOriginal.entrySet()) {
                mapResult.put(entry.getKey(), convert(entry.getValue()));
            }
            return mapResult;
        }
        return original;
    }
}
