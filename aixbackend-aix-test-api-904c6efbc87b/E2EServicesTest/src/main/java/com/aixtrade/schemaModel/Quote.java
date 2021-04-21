package com.aixtrade.schemaModel;

import lombok.Data;

@Data
public class Quote {

    private String bidPrice;
    private String bidQuantity;
    private String expirationInSeconds;
    private String offerPrice;
    private String offerQuantity;
    private Long quoteRequestId;
    private Long quoteResponseId;
    private String symbol;
    private String trader;
}
