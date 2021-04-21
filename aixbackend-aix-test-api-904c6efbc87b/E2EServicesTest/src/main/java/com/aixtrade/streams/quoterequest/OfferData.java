package com.aixtrade.streams.quoterequest;

import lombok.Data;

import java.util.List;


@Data
public class OfferData {

    private Long price;
    private Boolean aggregated;
    private Boolean isQuote;
    private String aggregateOrderId;
    private List<OrderData> orders = null;
}
