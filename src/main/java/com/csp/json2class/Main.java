package com.csp.json2class;

import com.csp.json2class.dto.TestDto;
import com.csp.json2class.factory.AnnotationBeanFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-09 10:24
 * @version: 1.0
 */
public class Main {

    private static String json = "{\n" +
            "    \"echoCode\": 0,\n" +
            "    \"success\": true,\n" +
            "    \"page\": 0,\n" +
            "    \"pageSize\": 0,\n" +
            "    \"pageCount\": 1,\n" +
            "    \"total\": 1,\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"associateShop\": {\n" +
            "                \"tenant\": \"x\",\n" +
            "                \"id\": \"x\",\n" +
            "                \"orgType\": \"-\",\n" +
            "                \"orgId\": \"-\",\n" +
            "                \"code\": \"x\",\n" +
            "                \"name\": \"x\",\n" +
            "                \"type\": \"x\",\n" +
            "                \"contact\": {\n" +
            "                    \"telephone\": \"12345678910\"\n" +
            "                },\n" +
            "                \"address\": {\n" +
            "                    \"provinceName\": \"x\",\n" +
            "                    \"districtName\": \"x\",\n" +
            "                    \"streetName\": \"x\"\n" +
            "                },\n" +
            "                \"enabled\": true\n" +
            "            },\n" +
            "            \"code\": \"x\",\n" +
            "            \"name\": \"x\",\n" +
            "            \"state\": \"x\",\n" +
            "            \"businessState\": \"x\",\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";

    private static String func = "function trans(json, exr) {\n" +
            "\tvar obj = eval('(' + json + ')');\n" +
            "\treturn eval('obj.' + exr);\n" +
            "}";

    public static void main(String[] args) throws Exception {
        test2();
    }


    public static void test1() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.eval(func);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;
            Object val = invoke.invokeFunction("trans", json, "data[0].associateShop.contact.telephone");
            System.out.println(val);
        }
    }
    public static void test2() throws ScriptException, InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        TestDto testDto = new AnnotationBeanFactory(TestDto.class).createBean(TestDto.class, json);
        System.out.println(testDto.toString());
    }

    public static void test3() {
        Class targetClass = TestDto.class;
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                // 获取成员变量的泛型类型信息
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    Class fieldArgClass = (Class) actualTypeArgument;
                    System.out.println(fieldArgClass);
                }
            }
        }
    }
}
