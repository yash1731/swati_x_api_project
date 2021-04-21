package com.aixtrade.schemaModel;

import lombok.Data;

@Data
public class QuoteResponse {

    private String bidPrice;
    private String bidQuantity;
    private String expirationInSeconds;
    private String offerPrice;
    private String offerQuantity;
    private Integer quoteId;
    private String symbol;
    private String type;
    private String userId;
}
