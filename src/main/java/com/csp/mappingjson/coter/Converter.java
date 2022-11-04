package com.csp.mappingjson.coter;

import com.csp.mappingjson.model.JsonNode;

/**
 * @description: 数据转换器接口
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 15:12
 * @version: 1.0
 */
public interface Converter {

    Object doConverter(Object originVal, JsonNode jsonNode);
}
