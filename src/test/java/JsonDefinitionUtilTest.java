import com.csp.mappingjson.JsonDefinitionUtil;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-20 19:36
 * @version: 1.0
 */
public class JsonDefinitionUtilTest {

    private static final String result = "{\"root\":[{\"name\":\"arrDtos\",\"value\":null,\"type\":\"ARRAY\",\"converter\":\"\",\"sourceLocation\":\"\",\"actualType\":\"root.arrDtos\",\"enumMapping\":\"\"},{\"name\":\"name\",\"value\":\"haha\",\"type\":\"STRING\",\"converter\":\"\",\"sourceLocation\":\"\",\"actualType\":\"\",\"enumMapping\":\"\"},{\"name\":\"sonDto\",\"value\":null,\"type\":\"OBJECT\",\"converter\":\"\",\"sourceLocation\":\"\",\"actualType\":\"root.sonDto\",\"enumMapping\":\"\"}],\"root.arrDtos\":[{\"name\":\"code\",\"value\":\"1\",\"type\":\"STRING\",\"converter\":\"\",\"sourceLocation\":\"\",\"actualType\":\"\",\"enumMapping\":\"\"}],\"root.sonDto\":[{\"name\":\"age\",\"value\":18.0,\"type\":\"DOUBLE\",\"converter\":\"\",\"sourceLocation\":\"\",\"actualType\":\"\",\"enumMapping\":\"\"}]}";

    @Test
    public void test() {
        String json = "{\n" +
                "    \"arrDtos\": [\n" +
                "        {\n" +
                "        \t\"code\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "        \t\"code\": \"2\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"name\": \"haha\",\n" +
                "    \"sonDto\": {\n" +
                "        \"age\": 18\n" +
                "    }\n" +
                "}";
        /*Assert.assertEquals(result, JsonDefinitionUtil.parse(json));*/
    }
}
