package com.example;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.*;

import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.*;

public class DymanoDBUserItem {

    private static String DYNAMO_DB_TABLE_NAME = "IBelieveUserData";
    private static Region REGION = Region.US_WEST_1;




    public static void main(String[] args) {
        try{
            DymanoDBUserItem app = new DymanoDBUserItem();

            DynamoDbEnhancedClient enhancedClient  = app.initDynamoDbClient();

//            DynamoDbTable<UserItem> userTable = enhancedClient.table(DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(UserItem.class));
//            app.createTable(enhancedClient);
//            app.putTestUserData(userTable);
//            DynamoDbTable<StyleItem> styleTable = enhancedClient.table(DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(StyleItem.class));
//            app.putTestStyleData(styleTable, "1");
//
//            DynamoDbTable<RecItem>  recTable = enhancedClient.table(DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(RecItem.class));
//            app.putTestRecData(recTable, "2","3");
//
//            DynamoDbClient dbClient = buildDynamoDbClient();
//
//            app.queryTableFilter(enhancedClient);

            app.queryRECTableFilter(enhancedClient,"2","3");

//            app.deleteItem(enhancedClient, user);

           /* User getUser = new User();
            getUser.setUserName("Test");
            User result = app.getItem(enhancedClient, "UserName#Test34", "UserData#Test34");
            System.out.println(result.getEmailId());*/
//            app.putTestUserData(enhancedClient);
//            app.putStyleTestData("Test63" , enhancedClient);
//            app.putTestRecommendationData("Test63","StyleId#075e09dd-1a67-409d-af56-be4e3ca7d267", enhancedClient);
//            IBelieveData data = app.getItem(enhancedClient,"testTest1212", "Profile#Test0");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            System.out.println(gson.toJson(data));
//            data.setLastName("Jeeeeererereerer");
//            data.setLastUpdatedDate(Instant.now());
//            data.setCreatedDate(Instant.now());
//            app.updateItem(enhancedClient,data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static DynamoDbClient buildDynamoDbClient() {
        return  DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build();
    }


    public  void queryTableFilter(DynamoDbEnhancedClient enhancedClient) {

        try{
            DynamoDbTable<StyleItem> mappedTable = enhancedClient.table(DYNAMO_DB_TABLE_NAME, TableSchema.fromBean(StyleItem.class));
            // Type
            AttributeValue att = AttributeValue.builder()
                    .s("Style")
                    .build();

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":value", att);

            Expression expression1 = Expression.builder()
                    .expression("entityType = :value")
                    .expressionValues(expressionValues)
                    .build();

            // Create a QueryConditional object that is used in the query operation.
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(StyleItem.prefixedPKId("1"))
                            .build());

            // Get items in the Customer table and write out the ID value.
            Iterator<StyleItem> results = mappedTable.query(r -> r.queryConditional(queryConditional).filterExpression(expression1)).items().iterator();

            while (results.hasNext()) {

                StyleItem rec = results.next();
                System.out.println("The Style id is "+rec.getId() + "  UserId " + rec.getUserId());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }


    public  void queryRECTableFilter(DynamoDbEnhancedClient enhancedClient, String userId, String styleId) {

        try{
            DynamoDbTable<RecItem> mappedTable = enhancedClient.table(DYNAMO_DB_TABLE_NAME, TableSchema.fromBean(RecItem.class));
            // Type
            AttributeValue att = AttributeValue.builder()
                    .s("Rec")
                    .build();

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":value", att);

            Expression expression1 = Expression.builder()
                    .expression("entityType = :value")
                    .expressionValues(expressionValues)
                    .build();

            Expression expression3 = Expression.builder().putExpressionName("entityType", "Rec").build();

           AttributeValue att1 = AttributeValue.builder()
                    .s(styleId)
                    .build();

            Map<String, AttributeValue> expressionValues1 = new HashMap<>();
            expressionValues.put(":value", att1);

            Expression expression2 = Expression.builder()
                    .expression("styleId = :value")
                    .expressionValues(expressionValues)
                    .build();
            // Create a QueryConditional object that is used in the query operation.
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder().partitionValue(RecItem.prefixedPKId(userId))
                            .build());

            // Get items in the Customer table and write out the ID value.
            Iterator<RecItem> results = mappedTable.query(r -> r.queryConditional(queryConditional).filterExpression(Expression.builder().putExpressionName("entityType", "Rec").build()).filterExpression(expression2)).items().iterator();

            while (results.hasNext()) {

                RecItem rec = results.next();
                System.out.println("The REC id is "+rec.getId() + "  UserId " + rec.getUserId());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Done");
    }

    private void putTestUserData(DynamoDbTable<UserItem> userTable){
        UserItem user ;
        for (int i = 0; i < 5; i++) {
            user = new UserItem();
            user.setId(i+"");
            user.setType("User");
            user.setFirstName("Coo"+i);
            user.setLastName("VM"+i);
            user.setEmailId("coolvm"+i+"@gmail.com");
            user.setSubscribe(true);

            userTable.putItem(user);
        }
    }

    public UserItem getItemWithList(DynamoDbTable<UserItem> userTable ) {

        Key key = Key.builder()
                .partitionValue(AttributeValue.builder().s("ITEM#123").build())
                .sortValue(AttributeValue.builder().s("A").build())
                .build();
        return userTable.getItem(key);
    }

    private void putTestStyleData(DynamoDbTable<StyleItem> table, String userId){
        StyleItem entity ;
        for (int i = 0; i < 5; i++) {
            entity = new StyleItem();
            entity.setUserId(userId);
            entity.setId(i+"");
            entity.setType("Style");
            entity.setBust(i);
            table.putItem(entity);
        }
    }

    private void putTestRecData(DynamoDbTable<RecItem> table, String userId, String styleId){
        List<String> test = new ArrayList<>();
        test.add("Test1");
        RecItem entity ;
        for (int i = 0; i < 5; i++) {
            entity = new RecItem();
            entity.setUserId(userId);
            entity.setStyleId(styleId);
            entity.setId(i+"");
            entity.setType("Rec");
            entity.setApparelType(test);
            table.putItem(entity);
        }
    }

    private void createTable(DynamoDbEnhancedClient enhancedClient) throws Exception {
        // Maps a physical table with the name 'customers_20190205' to the schema
        DynamoDbTable<UserItem> userTable = enhancedClient.table(DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(UserItem.class));
        userTable.createTable();
    }

    private void putItem(DynamoDbEnhancedClient enhancedClient, UserItem newUser) throws Exception {
        DynamoDbTable<UserItem> userTable = enhancedClient.table(DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(UserItem.class));
        userTable.putItem(newUser);
    }




    private DynamoDbEnhancedClient initDynamoDbClient() {

/*
        DynamoDbClientBuilder builder = DynamoDbClient.builder();
//        builder.httpClient(ApacheHttpClient.builder().build());
        builder.endpointOverride(URI.create("http://dynamodb-local:8000"));
        builder.region(Region.US_WEST_1);
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(builder.build())
                .build();
*/

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDbClient.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("v3409", "iphjx")))
                        .region(Region.US_WEST_1)
//                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .endpointOverride(URI.create("http://localhost:8000"))
                        .build())
                .build();
    }
}
