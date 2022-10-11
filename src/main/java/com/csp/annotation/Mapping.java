package com.csp.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-09 16:32
 * @version: 1.0
 */
@Inherited
@Target({ElementType.ANNOTATION_TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    String targetLocation() default "";
    String converterReference() default "com.csp.js.coter.DefaultConverter";
    boolean isIgnore() default false;
}
