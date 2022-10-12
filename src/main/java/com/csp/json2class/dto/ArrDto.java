package com.csp.json2class.dto;

import com.csp.js.annotation.Mapping;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-09 20:29
 * @version: 1.0
 */
public class ArrDto {

    @Mapping(targetLocation = "code")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ArrDto{" +
                "code=" + code +
                '}';
    }
}
