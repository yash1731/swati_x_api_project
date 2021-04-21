package com.aixtrade.streams.quoteresponse;

import com.aixtrade.streams.quote.QuoteOrderData;
import lombok.Data;

import java.util.List;

@Data
public class QuoteResponseOfferData {

    private Long price;
    private Long executedPrice;
    private Long quantity;
    private Long liveQuantity;
    private Long executedQuantity;
    private Long accumulatedExecutedQuantity;
    private Boolean aggregated;
    private Boolean isQuote;
    private Boolean isSelfImproved;
    private String aggregateOrderId;
    private List<QuoteOrderData> orders = null;
}
