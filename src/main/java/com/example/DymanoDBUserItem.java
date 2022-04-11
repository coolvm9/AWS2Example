package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DymanoDBUserItem {

    private String DYNAMO_DB_TABLE_NAME = "IBelieveUserData";
    private Region REGION = Region.US_WEST_1;


    public static void main(String[] args) {
        try{
            DymanoDBUserItem app = new DymanoDBUserItem();
            DynamoDbEnhancedClient enhancedClient  = app.initDynamoDbClient();
            DynamoDbTable<UserItem> userTable = enhancedClient.table(app.DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(UserItem.class));
            app.createTable(enhancedClient);
            app.putTestUserData(userTable);
            DynamoDbTable<StyleItem> styleTable = enhancedClient.table(app.DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(StyleItem.class));
            app.putTestStyleData(styleTable, "1");

            DynamoDbTable<RecItem>  recTable = enhancedClient.table(app.DYNAMO_DB_TABLE_NAME, TableSchema.fromClass(RecItem.class));
            app.putTestRecData(recTable, "1","1");
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
