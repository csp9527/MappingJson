package com.csp.json2json;

import com.csp.json2class.dto.ArrDto;
import com.csp.json2class.dto.SonDto;
import com.csp.json2class.dto.TestDto;
import com.csp.json2json.model.Node;
import com.google.gson.Gson;

import java.util.*;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-12 20:14
 * @version: 1.0
 */
public class Test {

    private static Gson gson = new Gson();

    private static Map<String, List<Node>> jsonDefinitionMap = new LinkedHashMap<>();

    public static void main(String[] args) {
        ArrDto arrDto1 = new ArrDto();
        arrDto1.setCode("0");
        ArrDto arrDto2 = new ArrDto();
        arrDto2.setCode("1");
        List<ArrDto> arrDtoList = new ArrayList<>();
        arrDtoList.add(arrDto1);
        arrDtoList.add(arrDto2);

        SonDto sonDto = new SonDto();
        sonDto.setAge(18);

        TestDto testDto = new TestDto();
        testDto.setName("haha");
        testDto.setSonDto(sonDto);
        testDto.setArrDtos(arrDtoList);

        String json = gson.toJson(testDto);

        System.out.println(json);

        Map map = gson.fromJson(json, Map.class);
        System.out.println(map);
        parse(map, "root");

        System.out.println(gson.toJson(jsonDefinitionMap));
    }

    public static void parse(Map root, String name) {
        List<Node> nodes = new ArrayList<>();
        jsonDefinitionMap.put(name, nodes);

        Set<String> set = root.keySet();
        for (String key : set) {

            Object val = root.get(key);
            Node node = new Node();
            node.setName(key);
            if (val instanceof Map) {
                node.setType(key);
                parse((Map) val, key);
            } else if (val instanceof List) {
                node.setType("array");
                Object innerVal = ((List) val).get(0);
                if (innerVal instanceof Map) {
                    node.setActualType(key);
                    parse((Map) innerVal, key);
                }
            } else {
                node.setType("string");
            }
            nodes.add(node);
        }
    }

}
