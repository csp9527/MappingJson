package com.csp.json2class.dto;

import com.csp.js.annotation.Mapping;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-09 20:08
 * @version: 1.0
 */
public class SonDto {

    @Mapping(targetLocation = "total")
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "SonDto{" +
                "age=" + age +
                '}';
    }
}
