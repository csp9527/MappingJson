package com.csp.mappingjson.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description: json节点
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 10:36
 * @version: 1.0
 */
@Setter
@Getter
@ToString
public class JsonNode {
    // 属性名
    private String name = "";
    // 默认值
    private Object value = null;
    // 类型
    private String type = "";
    // 转换器(类限定名)
    private String converter = "";
    // 映射属性定位
    private String sourceLocation = "";

    // 当type为Array时，对应的实际类型
    private String actualType = "";
    // 枚举映射
    private String enumMapping = "";
}
