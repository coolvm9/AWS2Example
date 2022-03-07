package com.example;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

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

            User getUser = new User();
            getUser.setUserName("Test");
            User result = app.getItem(enhancedClient, "UserName#Test34", "UserData#Test34");
            System.out.println(result.getEmailId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void putTestData(DynamoDbEnhancedClient enhancedClient){
        TableSchema<User> USER_TABLE_SCHEMA = TableSchema.fromClass(User.class);
        DynamoDbTable<User> userTable = enhancedClient.table("User", USER_TABLE_SCHEMA);
        User user = new User();
        for (int i = 0; i < 100; i++) {
            user = new User();
            user.setUserName("Test"+i);
            user.setMetadata("UserData#"+"Test"+i);
            user.setFirstName("Coo"+i);
            user.setLastName("VM"+i);
            user.setEmailId("coolvm"+i+"@gmail.com");
            user.setSubscribe(true);
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

    private User getItem(DynamoDbEnhancedClient enhancedClient, String userName, String metadata) throws Exception {
        TableSchema<User> USER_TABLE_SCHEMA = TableSchema.fromClass(User.class);
        DynamoDbTable<User> userTable = enhancedClient.table("User", TableSchema.fromBean(User.class));
        Key key = Key.builder()
                .partitionValue(userName)
                .sortValue(metadata)
                .build();
        return userTable.getItem(key);
    }

    private void deleteItem(DynamoDbEnhancedClient enhancedClient, User newUser) throws Exception {
        TableSchema<User> USER_TABLE_SCHEMA = TableSchema.fromClass(User.class);
        DynamoDbTable<User> userTable = enhancedClient.table("User", USER_TABLE_SCHEMA);
        userTable.deleteItem(newUser);
    }

    private DynamoDbEnhancedClient initDynamoDbClient() {
        DynamoDbClient ddb = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy-key", "dummy-secret")))
                .build();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
        return enhancedClient;
    }
}
