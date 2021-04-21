package com.aixtrade.streams.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStreamData {
    private String orderId;
    private String symbol;
    private BigDecimal price;
    private String side;
    private Long quantity;
    private Long originalQuantity;
    private Long liveQuantity;
    private Long executionQuantity;
    private Long minimumQuantity;
    private Long displayQuantity;
    private Long rejectQuantity;
    private String trader;
    private String orderType;
    private String orderStatus;
    private Long expireAtInMillis;
    private String userId;
    private Long enteredAtInMillis;
    private Long updatedAtInMillis;
    private Long receivedAtInMillis;
    private Boolean isQuote;
    private Boolean isRestingOrder;
    private String quoteResponseId;
    private String quoteId;
    private Boolean isAggregated;
}
