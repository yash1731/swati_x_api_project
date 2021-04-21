package com.aixtrade.streams.quote;

import lombok.Data;

import java.util.List;


@Data
public class QuoteOfferData {

    private Long price;
    private Long executedPrice;
    private Long quantity;
    private Long liveQuantity;
    private Long executedQuantity;
    private Boolean aggregated;
    private Boolean isQuote;
    private Boolean isSelfImproved;
    private String aggregateOrderId;
    private List<QuoteOrderData> orders = null;
}
