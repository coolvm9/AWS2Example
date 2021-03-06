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
public class StyleItem {

    static final String ENTITY_RECORD = "S";
    static final String ENTITY_TYPE = "Style";
    private static final String PK_ENTITY_PREFIX = "USER#";
    private static final String SK_ENTITY_PREFIX = "STYLE#";

    private String id;
    private String type;

    private String userId;



    static String prefixedPKId(String id) {
        return PK_ENTITY_PREFIX + id;
    }
    static String prefixedSKId(String id) {
        return SK_ENTITY_PREFIX + id;
    }

    @DynamoDbAttribute("styleId")
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
            throw new IllegalArgumentException("Attempted marshall into Style an item of Type=" + type);
        }
    }

    private List<String> occasion;
    private List<String> dressingStyle;
    private List<String> weather;
    private List<String> weatherCondition;
    private int height;
    private int bust;
    private int waist;
    private int hip;
    private int inseam;

}
