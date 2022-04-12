package com.example;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@DynamoDbBean
public class UserItem {

    static final String ENTITY_RECORD = "A";
    static final String ENTITY_TYPE = "User";
    private static final String ENTITY_PREFIX = "USER#";

    private String id;
    private String type;

    static String prefixedId(String id) {
        return ENTITY_PREFIX + id;
    }

    @DynamoDbAttribute("userId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPartitionKey() {
        return prefixedId(this.id);
    }

    public void setPartitionKey(String partitionKey) {
        // Do nothing, this is a derived attribute
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSortKey() {
        return ENTITY_RECORD;
    }

    public void setSortKey(String sortKey) {
        // Do nothing, this is a derived attribute
    }

    @DynamoDbAttribute("entityType")
    public String getType() {
        return ENTITY_TYPE;
    }

    // Do nothing, this is a derived attribute
    // Note that without the setter, the attribute will silently not be persisted by the Enhanced Client
    public void setType(String type) {
        if (!ENTITY_TYPE.equals(type)) {
            // This can happen when performing a Scan on a table of heterogeneous items
            throw new IllegalArgumentException("Attempted marshall into Customer an item of Type=" + type);
        }
    }

    private  String firstName;
    private String lastName;
    private String emailId;
    private boolean subscribe;
}
