import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;

public class Main {
    static String TABLE_NAME = "movies";

    public static void main(String[] args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:8000", "ap-south-1"))
                .withCredentials(new ProfileCredentialsProvider("F:\\document\\DynamoDB\\local\\credentials", "default"))
                .build();

        /*AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://dynamodb.cn-northwest-1.amazonaws.com.cn", "cn-northwest-1"))
                .withCredentials(new ProfileCredentialsProvider("F:\\document\\DynamoDB\\web\\credentials", "cn-northwest-1"))
                .build();*/

        DynamoDB dynamoDB = new DynamoDB(client);

        try {
            //delete(dynamoDB);
            //createTable(dynamoDB);
            //putItems(dynamoDB);
            scanLocal(client);
            //scan(client);
            //scan2(dynamoDB);
            //batchWrite(client);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //DynamoDBMapper mapper = new DynamoDBMapper(db);


    }

    static void createTable(DynamoDB dynamoDB) throws Exception {
        List<KeySchemaElement> keyList = new ArrayList<KeySchemaElement>();
        keyList.add(new KeySchemaElement("year", KeyType.HASH));
        keyList.add(new KeySchemaElement("title", KeyType.RANGE));

        List<AttributeDefinition> attrList = new ArrayList<AttributeDefinition>();
        attrList.add(new AttributeDefinition("year", ScalarAttributeType.N));
        attrList.add(new AttributeDefinition("title", ScalarAttributeType.S));

        Table table = dynamoDB.createTable(TABLE_NAME,
                keyList, attrList, new ProvisionedThroughput(10L, 10L));
        table.waitForActive();
        System.out.println("create success");
    }

    static void putItems(DynamoDB dynamoDB) throws Exception {
        Table table = dynamoDB.getTable(TABLE_NAME);

        JsonParser parser = new JsonFactory().createParser(
                new File("F:\\document\\DynamoDB\\moviedata\\part2.json"));

        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();

        ObjectNode currentNode;

        while (iter.hasNext()) {
            currentNode = (ObjectNode) iter.next();

            int year = currentNode.path("year").asInt();
            String title = currentNode.path("title").asText();


            table.putItem(new Item().withPrimaryKey("year", year, "title", title).withJSON("info",
                        currentNode.path("info").toString()));
            System.out.println("PutItem succeeded: " + year + " " + title);
        }
        parser.close();
    }

    static void scan(AmazonDynamoDB dynamoDB) throws Exception {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        Map<String, AttributeValue> lastKey = null;
        List<AccountItem> list = null;

        //do {
            DynamoDBScanExpression expression = new DynamoDBScanExpression().withLimit(2);
            if (lastKey != null) expression.setExclusiveStartKey(lastKey);
            ScanResultPage<AccountItem> result = mapper.scanPage(AccountItem.class, expression,
                    getConfig(AccountItem.class, null, null));
            list = result.getResults();
            lastKey = result.getLastEvaluatedKey();
            System.out.println(lastKey);


            System.out.println("size:" + list.size());
            for (int i = 0; i < list.size(); ++i) {
                //System.out.println("year:" + list.get(i).getYear() + " 　title:" + list.get(i).getTitle());
            }
        //} while (list != null && list.size() > 0);
    }

    static void scanLocal(AmazonDynamoDB dynamoDB) throws Exception {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        Map<String, AttributeValue> lastKey = null;
        List<Movie> list = null;

        DynamoDBScanExpression expression = new DynamoDBScanExpression();
        expression.withLimit(3);
        ScanResultPage<Movie> result = mapper.scanPage(Movie.class, expression, null);
        list = result.getResults();
        lastKey = result.getLastEvaluatedKey();



        String json = JSON.toJSONString(lastKey);
        System.out.println("json:" + json);
        Map<String, AttributeValue> fan =
        JSON.parseObject(json, new TypeReference<Map<String, AttributeValue>>(String.class, AttributeValue.class) {});
        System.out.println("fan:" +fan);


        if (false) return;
        //System.out.println(lastKey);

        System.out.println("size:" + list.size());
        for (int i = 0; i < 3; ++i) {
            System.out.println(list.get(i).toString());
        }

        System.out.println("start second--------------------------");
        expression = new DynamoDBScanExpression().withExclusiveStartKey(fan).withLimit(3);
        result = mapper.scanPage(Movie.class, expression, null);
        list = result.getResults();
        lastKey = result.getLastEvaluatedKey();

        System.out.println(lastKey);

        System.out.println("size:" + list.size());
        for (int i = 0; i < 3; ++i) {
            System.out.println("year:" + list.get(i).getYear() + " 　title:" + list.get(i).getTitle());
        }
    }

    static void scan2(DynamoDB dynamoDB) throws Exception {
        ScanSpec scanSpec = new ScanSpec();
        Table table = dynamoDB.getTable(TABLE_NAME);
        ItemCollection<ScanOutcome> items = table.scan(scanSpec);

        for (Item item : items) {
            System.out.println(item.toJSON());
        }
    }

    static void delete(DynamoDB dynamoDB) throws Exception {
        Table table = dynamoDB.getTable(TABLE_NAME);
        table.delete();
        table.waitForDelete();
        System.out.println("delete success");
    }

    static DynamoDBMapperConfig getConfig(Class clazz, String env, DynamoDBMapperConfig.SaveBehavior value) {
        DynamoDBTable annotation = (DynamoDBTable) clazz.getAnnotation(DynamoDBTable.class);
        String prefix = "ikylin_test_";

        DynamoDBMapperConfig.TableNameOverride tbl = new DynamoDBMapperConfig.TableNameOverride(annotation.tableName()).withTableNamePrefix(prefix);
        return new DynamoDBMapperConfig
                .Builder()
                .withTableNameOverride(tbl)
                .withSaveBehavior(value)
                .build();

    }

    static void batchWrite(AmazonDynamoDB amazonDynamoDB) {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        Movie movie = new Movie();
        movie.setYear((long) 2004);
        movie.setTitle("The Incredibles");
        movie.setOne("1");
        movie.setTwo("2");
        movie.setThree("3");
        List<Movie> list = new ArrayList<Movie>();
        list.add(movie);
        mapper.batchWrite(list, Collections.emptyList());
    }

}
