package com.aixtrade.streams.quote;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteStreamData {

    private Integer quoteId;
    private Long quoteResponseId;
    private Long quoteRequestId;
    private String symbol;
    private Long requestedQuantity;
    private String channel;
    private String direction;
    private String trader;
    private String status;
    private Boolean unsolicited;
    private QuoteBidData bid;
    private QuoteOfferData offer;
    private Long expirationInSeconds;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    private LocalDateTime expiresAt;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    private LocalDateTime cancelledAt;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    private LocalDateTime expiredAt;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    private LocalDateTime createdAt;
//    //    private String createdAt;
    private String name;
}
