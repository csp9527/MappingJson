package com.csp.json2class.factory;

import com.csp.json2class.coter.Converter;
import com.csp.json2class.engine.JsonParserEngine;
import com.csp.json2class.engine.MappingDefinition;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-09 16:31
 * @version: 1.0
 */
public abstract class BeanFactory {

    private final Map<String, MappingDefinition> mappingDefinitionMap = new ConcurrentHashMap<>(256);
    private final Map<String, Converter> converterMap = new ConcurrentHashMap<>(256);

    private static final String TARGET_LOCATION_PREFIX = "#";

    private static final String ARRAY_MARK = "[?]";
    private static final String PLACEHOLDER = "?";

    private static final String METHOD_PREFIX = "set";
    public static final String POINT = ".";

    private static String sourceJson;

    public  <T> T createBean(Class<T> targetClass, String sourceJson) throws ScriptException, InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchMethodException {
        this.sourceJson = sourceJson;
        return this.createBean(targetClass, "", "");
    }

    private <T> T createBean(Class<T> targetClass, String parentSourceLocation, String parentTargetLocation) throws InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, ScriptException {
        T t = targetClass.newInstance();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            String currentSourceLocation = getCurrentSourceLocation(parentSourceLocation, fieldName);
            MappingDefinition mappingDefinition = this.mappingDefinitionMap.get(currentSourceLocation);

            String currentTargetLocation = getCurrentTargetLocation(parentTargetLocation, mappingDefinition.getTargetLocation());
            String converterReference = mappingDefinition.getConverterReference();

            Converter converter = this.converterMap.get(converterReference);

            String methodName = getMethodName(fieldName);
            Method method = targetClass.getDeclaredMethod(methodName, field.getType());
            Object parsedVal;
            if (field.getType().isPrimitive() || field.getType() == String.class) {
                parsedVal = JsonParserEngine.parser(sourceJson, currentTargetLocation);
            } else if(field.getType() == List.class) {
                List list = new ArrayList();
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    // 获取成员变量的泛型类型信息
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    Class fieldArgClass = (Class) actualTypeArguments[0];

                    Object[] objs = null;
                    Object innerParsedVal = JsonParserEngine.parser(sourceJson, currentTargetLocation);
                    if (((ScriptObjectMirror) innerParsedVal).isArray()) {
                        objs = ((ScriptObjectMirror) innerParsedVal).values().toArray();
                    }

                    for (int i = 0; i < objs.length ; i++) {
                        currentTargetLocation = currentTargetLocation + ARRAY_MARK.replace(PLACEHOLDER, String.valueOf(i));
                        Object val = createBean(fieldArgClass, currentSourceLocation, currentTargetLocation);
                        list.add(val);
                    }
                }
                parsedVal = list;
            } else {
                parsedVal = createBean(field.getType(), currentSourceLocation, currentTargetLocation);
            }
            parsedVal = converter.doConverter(parsedVal);
            method.invoke(t, parsedVal);
        }
        return t;
    }

    private String getCurrentTargetLocation(String parentTargetLocation, String targetLocation) {
        if (targetLocation.startsWith(TARGET_LOCATION_PREFIX)) {
            return targetLocation.substring(1);
        }
        if (parentTargetLocation == null || "".equals(parentTargetLocation)) {
            return targetLocation;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parentTargetLocation).append(POINT).append(targetLocation);
        return sb.toString();
    }


    private String getMethodName(String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append(METHOD_PREFIX);
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return sb.toString();
    }

    protected String getCurrentSourceLocation(String parentSourceLocation, String fieldName) {
        if (parentSourceLocation == null || "".equals(parentSourceLocation)) {
            return fieldName;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(parentSourceLocation).append(POINT).append(fieldName);
        return sb.toString();
    }

    public void registerMappingDefinition(String sourceLocation, MappingDefinition mappingDefinition) {
        this.mappingDefinitionMap.put(sourceLocation, mappingDefinition);
    }

    public boolean containsMappingDefinition(String sourceLocation) {
        return this.mappingDefinitionMap.containsKey(sourceLocation);
    }

    public void registerConverter(String converterReference, Converter converter) {
        this.converterMap.put(converterReference, converter);
    }

    public boolean containsConverter(String converterReference) {
        return this.converterMap.containsKey(converterReference);
    }
}
