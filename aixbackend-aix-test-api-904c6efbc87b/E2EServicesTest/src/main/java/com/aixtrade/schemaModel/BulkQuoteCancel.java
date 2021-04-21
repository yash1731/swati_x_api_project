package com.aixtrade.schemaModel;


import lombok.Data;

@Data
public class BulkQuoteCancel {
    private String side;
    private String symbol;
    private String userId;
}
