package com.csp.mappingjson.coter;

import com.csp.mappingjson.model.JsonNode;
import com.csp.mappingjson.model.NodeType;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @description: 默认数据转换器
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 15:43
 * @version: 1.0
 */
public class DefaultConverter implements Converter {
    @Override
    public Object doConverter(Object originVal, JsonNode jsonNode) {
        try {
            if (originVal == null) {
                return originVal;
            }
            // 获取枚举映射
            Object targetVal;
            String enumMapping = jsonNode.getEnumMapping();
            if (StringUtils.isNotBlank(enumMapping)) {
                Map<String, Object> enumMap = new Gson().fromJson(enumMapping, Map.class);
                targetVal = enumMap.get(originVal.toString());
            } else {
                targetVal = originVal;
            }

            String nodeType = jsonNode.getType();
            if (NodeType.STRING.equals(nodeType)) {
                targetVal = String.valueOf(targetVal);
            } else if (NodeType.DOUBLE.equals(nodeType)) {
                targetVal = Double.valueOf(targetVal.toString());
            } else if (NodeType.INTEGER.equals(nodeType)) {
                if (targetVal instanceof Double) {
                    targetVal = ((Double) targetVal).intValue();
                } else if (targetVal instanceof Float) {
                    targetVal = ((Float) targetVal).intValue();
                } else {
                    targetVal = Integer.valueOf(targetVal.toString());
                }
            } else if (NodeType.BOOLEAN.equals(nodeType)) {
                targetVal = Boolean.valueOf(targetVal.toString());
            }
            return targetVal;
        } catch (Exception e) {
            throw new RuntimeException("数据值转换异常", e);
        }
    }
}
