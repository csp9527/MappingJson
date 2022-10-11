package com.csp.factory;

import com.csp.annotation.Mapping;
import com.csp.coter.Converter;
import com.csp.engine.MappingDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-10 14:40
 * @version: 1.0
 */
public class AnnotationBeanFactory extends BeanFactory {

    public AnnotationBeanFactory(Class clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.loadMappingDefinition(clazz, "");
    }

    private void loadMappingDefinition(Class clazz, String parentSourceLocation) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String sourceLocation = getCurrentSourceLocation(parentSourceLocation, fieldName);

            Mapping mapping = field.getAnnotation(Mapping.class);
            MappingDefinition mappingDefinition = new MappingDefinition();
            if (mapping != null) {
                mappingDefinition.setTargetLocation(mapping.targetLocation());
                mappingDefinition.setConverterReference(mapping.converterReference());
                mappingDefinition.setIgnore(mapping.isIgnore());
            }

            this.registerMappingDefinition(sourceLocation, mappingDefinition);

            String converterReference = mappingDefinition.getConverterReference();

            if (!this.containsConverter(converterReference)) {
                Class conClazz = Class.forName(converterReference);
                Converter converter = (Converter) conClazz.newInstance();
                this.registerConverter(converterReference, converter);
            }

            if (field.getType().isPrimitive() || field.getType() == String.class) {
                continue;
            } else if(field.getType() == List.class) {
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    // 获取成员变量的泛型类型信息
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    Class fieldArgClass = (Class) actualTypeArguments[0];
                    this.loadMappingDefinition(fieldArgClass, sourceLocation);
                }
            } else {
                this.loadMappingDefinition(field.getType(), sourceLocation);
            }
        }
    }

}
