package com.aixtrade.streams.quote;


import lombok.Data;


@Data
public class QuoteOrderData {

    private String id;
    private Long price;
    private String status;
    private Boolean isQuote;
    private Long liveQuantity;
    private String userId;
}
