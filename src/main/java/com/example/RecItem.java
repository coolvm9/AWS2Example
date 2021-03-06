package com.example;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@DynamoDbBean
public class RecItem {

    static final String ENTITY_RECORD = "R";
    static final String ENTITY_TYPE = "Rec";
    private static final String PK_ENTITY_PREFIX = "USER#";
    private static final String SK_ENTITY_PREFIX = "REC#";

    private String id;
    private String type;

    private String userId;
    private String styleId;



    static String prefixedPKId(String id) {
        return PK_ENTITY_PREFIX + id;
    }
    static String prefixedSKId(String id) {
        return SK_ENTITY_PREFIX + id;
    }

    @DynamoDbAttribute("recId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPartitionKey() {
        return prefixedPKId(this.userId);
    }

    public void setPartitionKey(String partitionKey) {
        // Do nothing, this is a derived attribute
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSortKey() {
        return prefixedSKId(id);
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

    private List<String> apparelType;


}
