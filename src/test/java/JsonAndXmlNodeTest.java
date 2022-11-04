import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-24 14:30
 * @version: 1.0
 */
public class JsonAndXmlNodeTest {

    String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root></root>";
    String json1 = "{\"root\":\"\"}";

    String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root code=\"1\"/>";
    String json2 = "{\"root\":{\"_code\":\"1\"}}";

    String xml3 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root>123456</root>";
    String json3 = "{\"root\":\"123456\"}";

    String xml4 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root code=\"1\">123456</root>";
    String json4 = "{\"root\":{\"_code\":\"1\",\"$key\":\"123456\"}}";

    String xml5 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root code=\"1\"><rds><rd id=\"123\"><age>20</age><name>haha</name></rd><rd id=\"456\"><age>25</age><name>hehe</name></rd></rds></root>";
    String json5 = "{\"root\":{\"_code\":\"1\",\"rds\":{\"rd\":[{\"_id\":\"123\",\"age\":\"20\",\"name\":\"haha\"},{\"_id\":\"456\",\"age\":\"25\",\"name\":\"hehe\"}]}}}";

    String xml6 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root code=\"1\"><rds><rd id=\"123\">20</rd><rd id=\"456\"><age>25</age><name>hehe</name></rd></rds></root>";
    String json6 = "{\"root\":{\"_code\":\"1\",\"rds\":{\"rd\":[{\"_id\":\"123\",\"$key\":\"20\"},{\"_id\":\"456\",\"age\":\"25\",\"name\":\"hehe\"}]}}}";

    String xml7 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root code=\"1\">haha<rds><rd id=\"123\"><age>20</age><name>haha</name></rd><rd id=\"456\"><age>25</age><name>hehe</name></rd></rds></root>";
    String json7 = "{\"root\":{\"$key\":\"haha\",\"_code\":\"1\",\"rds\":{\"rd\":[{\"_id\":\"123\",\"age\":\"20\",\"name\":\"haha\"},{\"_id\":\"456\",\"age\":\"25\",\"name\":\"hehe\"}]}}}";

    String xml8 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root code=\"1\">haha<rds><rd id=\"123\">20</rd><rd id=\"456\"><age>25</age><name>hehe</name></rd></rds></root>";
    String json8 = "{\"root\":{\"$key\":\"haha\",\"_code\":\"1\",\"rds\":{\"rd\":[{\"_id\":\"123\",\"$key\":\"20\"},{\"_id\":\"456\",\"age\":\"25\",\"name\":\"hehe\"}]}}}";

    String xml9 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root code=\"1\">haha hehe<rds><rd id=\"123\">20</rd><rd id=\"456\"><age>25</age><name>hehe</name></rd></rds></root>";
    String json9 = "{\"root\":{\"$key\":\"haha hehe\",\"_code\":\"1\",\"rds\":{\"rd\":[{\"_id\":\"123\",\"$key\":\"20\"},{\"_id\":\"456\",\"age\":\"25\",\"name\":\"hehe\"}]}}}";

    @Test
    public void xml2jsonTest() {
        List<String> xmls = new ArrayList<>();
        List<String> jsons = new ArrayList<>();

        xmls.add(xml1);
        xmls.add(xml2);
        xmls.add(xml3);
        xmls.add(xml4);
        xmls.add(xml5);
        xmls.add(xml6);
        xmls.add(xml7);
        xmls.add(xml8);
        xmls.add(xml9);

        jsons.add(json1);
        jsons.add(json2);
        jsons.add(json3);
        jsons.add(json4);
        jsons.add(json5);
        jsons.add(json6);
        jsons.add(json7);
        jsons.add(json8);
        jsons.add(json9);

        /*Xml2JsonNode node = new Xml2JsonNode();
        JsonObject params = new JsonObject();
        JsonObject p = new JsonObject();
        p.addProperty("value", "[\"rd\"]");
        params.add("eleNameOfArrTypeList", p);

        p = new JsonObject();
        p.addProperty("value", "nodeCode.data");
        params.add("sourceXmlPath", p);

        node.setParams(params.toString());

        for (int i = 0; i < xmls.size(); i++) {
            String xml = xmls.get(i);
            String json = jsons.get(i);
            TaskContext taskContext = new TaskContext(null);
            taskContext.add("nodeCode", NodeResult.success(node, xml));

            String v = node.process(taskContext).get("data").getAsString();
            Assert.assertEquals(json, v);
        }*/
    }

    @Test
    public void json2xmlTest() {
        List<String> xmls = new ArrayList<>();
        List<String> jsons = new ArrayList<>();

        xmls.add(xml1);
        xmls.add(xml2);
        xmls.add(xml3);
        xmls.add(xml4);
        xmls.add(xml5);
        xmls.add(xml6);
        xmls.add(xml7);
        xmls.add(xml8);
        xmls.add(xml9);

        jsons.add(json1);
        jsons.add(json2);
        jsons.add(json3);
        jsons.add(json4);
        jsons.add(json5);
        jsons.add(json6);
        jsons.add(json7);
        jsons.add(json8);
        jsons.add(json9);

        /*Json2XmlNode node = new Json2XmlNode();
        JsonObject params = new JsonObject();
        JsonObject p = new JsonObject();
        p.addProperty("value", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        params.add("xmlHeader", p);

        p = new JsonObject();
        p.addProperty("value", "nodeCode.data");
        params.add("sourceJsonPath", p);

        node.setParams(params.toString());

        for (int i = 0; i < jsons.size(); i++) {
            String xml = xmls.get(i);
            String json = jsons.get(i);
            TaskContext taskContext = new TaskContext(null);
            taskContext.add("nodeCode", NodeResult.success(node, json));

            String v = node.process(taskContext).get("data").getAsString();
            Assert.assertEquals(xml, v);
        }*/
    }
}
