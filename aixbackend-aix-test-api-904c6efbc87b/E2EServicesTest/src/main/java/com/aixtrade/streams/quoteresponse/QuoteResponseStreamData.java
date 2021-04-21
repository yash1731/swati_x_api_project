package com.aixtrade.streams.quoteresponse;

import lombok.Data;

@Data
public class QuoteResponseStreamData {

    private Long quoteResponseId;
    private Long quoteId;
    private Long quoteRequestId;
    private String symbol;
    private Long quantity;
    private Long requestedQuantity;
    private String channel;
    private String direction;
    private String trader;
    private String status;
    private QuoteResponseBidData bid;
    private QuoteResponseOfferData offer;
    private Long expirationInSeconds;
    private String expiresAt;
    private String cancelledAt;
    private String expiredAt;
    private String createdAt;
    private String name;
}
