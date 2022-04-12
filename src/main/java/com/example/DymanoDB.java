package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

public class DymanoDB {

    private String DYNAMO_DB_TABLE_NAME = "User";
    private Region REGION = Region.US_WEST_1;


    public static void main(String[] args) {
        try{
            DymanoDB app = new DymanoDB();
            DynamoDbEnhancedClient enhancedClient  = app.initDynamoDbClient();
//            app.createTable(enhancedClient);

//            app.putTestData(enhancedClient);
//            app.deleteItem(enhancedClient, user);

           /* User getUser = new User();
            getUser.setUserName("Test");
            User result = app.getItem(enhancedClient, "UserName#Test34", "UserData#Test34");
            System.out.println(result.getEmailId());*/
//            app.putTestUserData(enhancedClient);
//            app.putStyleTestData("Test63" , enhancedClient);
//            app.putTestRecommendationData("Test63","StyleId#075e09dd-1a67-409d-af56-be4e3ca7d267", enhancedClient);
            IBelieveData data = app.getItem(enhancedClient,"testTest1212", "Profile#Test0");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(data));
//            data.setLastName("Jeeeeererereerer");
//            data.setLastUpdatedDate(Instant.now());
//            data.setCreatedDate(Instant.now());
//            app.updateItem(enhancedClient,data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void putStyleTestData(String UserId, DynamoDbEnhancedClient enhancedClient){
        TableSchema<IBelieveData> USER_TABLE_SCHEMA = TableSchema.fromClass(IBelieveData.class);
        DynamoDbTable<IBelieveData> styleQuizDynamoDbTable =
                enhancedClient.table("IBelieveData", TableSchema.fromBean(IBelieveData.class));
        IBelieveData styleQuiz ;
        String uuid1 = UUID.randomUUID().toString();
        for (int i = 1; i < 10; i++) {
            uuid1 = UUID.randomUUID().toString();
            styleQuiz = new IBelieveData();
            styleQuiz.setUserId(UserId);
            styleQuiz.setMetadata("StyleId#"+uuid1);
            styleQuiz.setHeight(10);
            styleQuiz.setCreatedDate(Instant.now());
            styleQuiz.setLastUpdatedDate(Instant.now());
            styleQuizDynamoDbTable.putItem(styleQuiz);
        }


    }
    private void putTestUserData(DynamoDbEnhancedClient enhancedClient){
        TableSchema<IBelieveData> USER_TABLE_SCHEMA = TableSchema.fromClass(IBelieveData.class);
        DynamoDbTable<IBelieveData> userTable = enhancedClient.table("IBelieveData", USER_TABLE_SCHEMA);
        IBelieveData user ;
        for (int i = 0; i < 100; i++) {
            user = new IBelieveData();
            user.setUserId("Test"+i);
            user.setMetadata("Profile#"+"Test"+i);
            user.setFirstName("Coo"+i);
            user.setLastName("VM"+i);
            user.setEmailId("coolvm"+i+"@gmail.com");
            user.setSubscribe(true);
            user.setCreatedDate(Instant.now());
            user.setLastUpdatedDate(Instant.now());
            userTable.putItem(user);
        }
    }

    private void putTestRecommendationData(String userId, String styleQuizId, DynamoDbEnhancedClient enhancedClient){
        TableSchema<IBelieveData> USER_TABLE_SCHEMA = TableSchema.fromClass(IBelieveData.class);
        DynamoDbTable<IBelieveData> userTable = enhancedClient.table("IBelieveData", USER_TABLE_SCHEMA);
        IBelieveData user ;
        String uuid1;
        for (int i = 0; i < 100; i++) {
            uuid1 = UUID.randomUUID().toString();
            user = new IBelieveData();
            user.setUserId(userId);
            user.setStyleId(styleQuizId);
            user.setMetadata("Rec#"+uuid1);
            user.setCreatedDate(Instant.now());
            user.setLastUpdatedDate(Instant.now());

            userTable.putItem(user);
        }
    }
    private void createTable(DynamoDbEnhancedClient enhancedClient) throws Exception {
        TableSchema<User> USER_TABLE_SCHEMA = TableSchema.fromClass(User.class);
        // Maps a physical table with the name 'customers_20190205' to the schema
        DynamoDbTable<User> userTable = enhancedClient.table("User", USER_TABLE_SCHEMA);
        userTable.createTable();
    }

    private void putItem(DynamoDbEnhancedClient enhancedClient, User newUser) throws Exception {
        TableSchema<User> USER_TABLE_SCHEMA = TableSchema.fromClass(User.class);
        DynamoDbTable<User> userTable = enhancedClient.table("User", USER_TABLE_SCHEMA);
        userTable.putItem(newUser);
    }

    private IBelieveData getItem(DynamoDbEnhancedClient enhancedClient, String userName, String metadata) throws Exception {
        TableSchema<IBelieveData> USER_TABLE_SCHEMA = TableSchema.fromClass(IBelieveData.class);
        DynamoDbTable<IBelieveData> userTable = enhancedClient.table("IBelieveData", TableSchema.fromBean(IBelieveData.class));
        Key key = Key.builder()
                .partitionValue(userName)
                .sortValue(metadata)
                .build();
        return userTable.getItem(key);
    }

    private IBelieveData updateItem(DynamoDbEnhancedClient enhancedClient, IBelieveData data) throws Exception {
        TableSchema<IBelieveData> USER_TABLE_SCHEMA = TableSchema.fromClass(IBelieveData.class);
        DynamoDbTable<IBelieveData> userTable = enhancedClient.table("IBelieveData", TableSchema.fromBean(IBelieveData.class));

        return userTable.updateItem(data);
    }
    private void deleteItem(DynamoDbEnhancedClient enhancedClient, User newUser) throws Exception {
        TableSchema<User> USER_TABLE_SCHEMA = TableSchema.fromClass(User.class);
        DynamoDbTable<User> userTable = enhancedClient.table("User", USER_TABLE_SCHEMA);
        userTable.deleteItem(newUser);
    }



    private DynamoDbEnhancedClient initDynamoDbClient() {
      /*  DynamoDbClient ddb = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy-key", "dummy-secret")))
                .build();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
        return enhancedClient;*/
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
