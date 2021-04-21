package com.aixtrade.streams.quoterequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequestStreamData {

    private Long quoteRequestId;
    private String symbol;
    private Long quantity;
    private String channel;
    private String direction;
    private String trader;
    private String status;
    private Double spotPrice;
    private String expiresAt;
    private LatestQuoteData latestQuote;
    private Long price;
    private String name;
}
