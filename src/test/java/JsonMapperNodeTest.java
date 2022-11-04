import com.csp.mappingjson.engine.JsJsonParserEngine;
import com.csp.mappingjson.engine.JsonParserEngine;
import com.csp.mappingjson.mapper.JsonMapper;
import com.csp.mappingjson.model.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 20:09
 * @version: 1.0
 */
public class JsonMapperNodeTest {

    private static Gson gson = new Gson();

    private static String targetJson = "{\n" +
            "    \"echoCode\": 0,\n" +
            "    \"success\": true,\n" +
            "    \"page\": 0,\n" +
            "    \"pageSize\": 0,\n" +
            "    \"pageCount\": 1,\n" +
            "    \"total\": 1,\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"$associateShop\": {\n" +
            "                \"tenant\": \"x\",\n" +
            "                \"id\": \"x\",\n" +
            "                \"orgType\": \"-\",\n" +
            "                \"orgId\": \"-\",\n" +
            "                \"code\": \"x\",\n" +
            "                \"name\": \"x\",\n" +
            "                \"type\": \"x\",\n" +
            "                \"contact\": {\n" +
            "                    \"telephone\": \"12345678910\"\n" +
            "                },\n" +
            "                \"address\": {\n" +
            "                    \"provinceName\": \"x\",\n" +
            "                    \"districtName\": \"x\",\n" +
            "                    \"streetName\": \"x\"\n" +
            "                },\n" +
            "                \"enabled\": true\n" +
            "            },\n" +
            "            \"code\": \"x\",\n" +
            "            \"name\": \"x\",\n" +
            "            \"state\": \"x\",\n" +
            "            \"businessState\": \"x\"\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";

    private String definitionJson = "{\n" +
            "    \"root\": [\n" +
            "        {\n" +
            "            \"actualType\": \"root.arrDtos\",\n" +
            "            \"converter\": \"\",\n" +
            "            \"enumMapping\": \"\",\n" +
            "            \"name\": \"arrDtos\",\n" +
            "            \"sourceLocation\": \"#nodeCode.data\",\n" +
            "            \"type\": \"ARRAY\",\n" +
            "            \"value\": null\n" +
            "        },\n" +
            "        {\n" +
            "            \"actualType\": \"\",\n" +
            "            \"converter\": \"\",\n" +
            "            \"enumMapping\": \"\",\n" +
            "            \"name\": \"name\",\n" +
            "            \"sourceLocation\": \"#nodeCode.data[0].$associateShop.contact.telephone\",\n" +
            "            \"type\": \"STRING\",\n" +
            "            \"value\": \"haha\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"actualType\": \"root.sonDto\",\n" +
            "            \"converter\": \"\",\n" +
            "            \"enumMapping\": \"\",\n" +
            "            \"name\": \"sonDto\",\n" +
            "            \"sourceLocation\": \"\",\n" +
            "            \"type\": \"OBJECT\",\n" +
            "            \"value\": null\n" +
            "        }\n" +
            "    ],\n" +
            "    \"root.arrDtos\": [\n" +
            "        {\n" +
            "            \"actualType\": \"\",\n" +
            "            \"converter\": \"\",\n" +
            "            \"enumMapping\": \"\",\n" +
            "            \"name\": \"code\",\n" +
            "            \"sourceLocation\": \"code\",\n" +
            "            \"type\": \"STRING\",\n" +
            "            \"value\": \"1\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"root.sonDto\": [\n" +
            "        {\n" +
            "            \"actualType\": \"\",\n" +
            "            \"converter\": \"\",\n" +
            "            \"enumMapping\": \"{'1':'19'}\",\n" +
            "            \"name\": \"age\",\n" +
            "            \"sourceLocation\": \"#nodeCode.total\",\n" +
            "            \"type\": \"INTEGER\",\n" +
            "            \"value\": 18.0\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private static final String result = "{\"sonDto\":{\"age\":19},\"name\":\"12345678910\",\"arrDtos\":[{\"code\":\"x\"}]}";

    @Test
    public void test() {
        /*long start = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++) {
            TaskContext taskContext = new TaskContext(null);
            taskContext.add("nodeCode", new JsonParser().parse(targetJson));

            JsonMappingNode node = new JsonMappingNode();
            JsonObject params = new JsonObject();
            JsonObject p = new JsonObject();
            p.addProperty("value", definitionJson);
            params.add("jsonNodeDefinition", p);
            node.setParams(params.toString());

            String val = node.process(taskContext).get("data").getAsString();
            Assert.assertEquals(result, val);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);*/
    }

    @Test
    public void multithreadTest() throws InterruptedException {
        int size = 100;
        String targetJson = "{\n" +
                "    \"name\": \"replaceIndex\"\n" +
                "}";
        String defJson = "{\n" +
                "    \"root\": [\n" +
                "        {\n" +
                "            \"actualType\": \"\",\n" +
                "            \"converter\": \"\",\n" +
                "            \"enumMapping\": \"\",\n" +
                "            \"name\": \"name\",\n" +
                "            \"sourceLocation\": \"#name\",\n" +
                "            \"type\": \"STRING\",\n" +
                "            \"value\": null\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        CyclicBarrier cyclicBarrier = new CyclicBarrier(size);

        for (int i = 0; i < size; i++) {
            TestWork worker = new TestWork(targetJson.replace("replaceIndex", "name" + i), defJson, cyclicBarrier);
            worker.start();
        }

        Thread.sleep(10000);

    }

    public class TestWork extends Thread {

        private String targetJson;
        private String defJson;
        private CyclicBarrier cyclicBarrier;

        public TestWork(String targetJson, String defJson, CyclicBarrier cyclicBarrier) {
            this.targetJson = targetJson;
            this.defJson = defJson;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
                Map<String, List<JsonNode>> jsonDefinitionMap  = gson.fromJson(defJson, new TypeToken<Map<String, List<JsonNode>>>() {}.getType());
                JsJsonParserEngine jsonParserEngine = new JsJsonParserEngine();
                jsonParserEngine.parser(targetJson);
                JsonMapper jsonMapper = new JsonMapper();
                String mapedJson = jsonMapper.mapping(jsonDefinitionMap, jsonParserEngine);
                System.out.println(mapedJson);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void toArrNodeTest() {
        String targetJson = "{\n" +
                "\t\"userName\": \"张三\",\n" +
                "\t\"year\": 25,\n" +
                "\t\"address\": \"拱墅新天地\"\n" +
                "}";
        String defJson = "{\n" +
                "    \"root\": [\n" +
                "        {\n" +
                "            \"actualType\": \"root.objArr\",\n" +
                "            \"converter\": \"\",\n" +
                "            \"enumMapping\": \"\",\n" +
                "            \"name\": \"objArr\",\n" +
                "            \"sourceLocation\": \"\",\n" +
                "            \"type\": \"ARRAY\",\n" +
                "            \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"actualType\": \"STRING\",\n" +
                "            \"converter\": \"\",\n" +
                "            \"enumMapping\": \"\",\n" +
                "            \"name\": \"strArr\",\n" +
                "            \"sourceLocation\": \"#address\",\n" +
                "            \"type\": \"ARRAY\",\n" +
                "            \"value\": null\n" +
                "        }\n" +
                "    ],\n" +
                "    \"root.objArr\": [\n" +
                "        {\n" +
                "            \"actualType\": \"\",\n" +
                "            \"converter\": \"\",\n" +
                "            \"enumMapping\": \"\",\n" +
                "            \"name\": \"name\",\n" +
                "            \"sourceLocation\": \"#userName\",\n" +
                "            \"type\": \"STRING\",\n" +
                "            \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"actualType\": \"\",\n" +
                "            \"converter\": \"\",\n" +
                "            \"enumMapping\": \"\",\n" +
                "            \"name\": \"age\",\n" +
                "            \"sourceLocation\": \"#year\",\n" +
                "            \"type\": \"STRING\",\n" +
                "            \"value\": null\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        System.out.println(targetJson);
        System.out.println(defJson);
        Map<String, List<JsonNode>> jsonDefinitionMap  = gson.fromJson(defJson, new TypeToken<Map<String, List<JsonNode>>>() {}.getType());
        JsonParserEngine jsonParserEngine = new JsJsonParserEngine();
        jsonParserEngine.parser(targetJson);
        JsonMapper jsonMapper = new JsonMapper();
        String mapedJson = jsonMapper.mapping(jsonDefinitionMap, jsonParserEngine);
        System.out.println(mapedJson);
    }
}
