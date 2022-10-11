package com.csp.dto;

import com.csp.js.annotation.Mapping;

import java.util.List;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-09 16:35
 * @version: 1.0
 */
public class TestDto {

    @Mapping(targetLocation = "data[0].associateShop.contact.telephone")
    private String name;

    @Mapping
    private SonDto sonDto;

    @Mapping(targetLocation = "data")
    private List<ArrDto> arrDtos;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SonDto getSonDto() {
        return sonDto;
    }

    public void setSonDto(SonDto sonDto) {
        this.sonDto = sonDto;
    }

    public List<ArrDto> getArrDtos() {
        return arrDtos;
    }

    public void setArrDtos(List<ArrDto> arrDtos) {
        this.arrDtos = arrDtos;
    }

    @Override
    public String toString() {
        return "TestDto{" +
                "name='" + name + '\'' +
                ", sonDto=" + sonDto +
                ", arrDtos=" + arrDtos +
                '}';
    }
}
