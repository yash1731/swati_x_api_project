package com.aixtrade.steps;

import com.aixtrade.streams.order.OrderStreamData;
import com.aixtrade.streams.quote.QuoteStreamData;
import com.aixtrade.streams.quoterequest.QuoteRequestStreamData;
import io.cucumber.java.DataTableType;

import java.math.BigDecimal;
import java.util.Map;

public class DataTables {

    @DataTableType
    public QuoteRequestStreamData defineQuoteRequestStreamData(Map<String, String> entry) {

        final QuoteRequestStreamData.QuoteRequestStreamDataBuilder streamDataBuilder =
                QuoteRequestStreamData
                        .builder()
                        .symbol(entry.get("symbol"))
                        .quantity(new Long(entry.get("quantity")))
                        .direction(entry.get("direction"))
                        .trader(entry.get("trader"))
                        .status(entry.get("status"));

        final String channel = entry.get("channel");

        if ("Telegram".equals(channel)) {
            streamDataBuilder.channel("T");
        } else if ("Symphony".equals(channel)) {
            streamDataBuilder.channel("S");

        } else if ("Fix".equals(channel)) {
            streamDataBuilder.channel("F");

        } else if ("Rest".equals(channel)) {
            streamDataBuilder.channel("R");

        }

        return streamDataBuilder.build();
    }

    @DataTableType
    public QuoteStreamData defineQuoteStreamData(Map<String, String> entry) {
        final QuoteStreamData.QuoteStreamDataBuilder streamDataBuilder =
                QuoteStreamData
                        .builder()
                        .symbol(entry.get("symbol"))
                        .requestedQuantity(new Long(entry.get("requestedQuantity")))
                        .direction(entry.get("direction"))
                        .trader(entry.get("trader"))
                        .status(entry.get("status"));

        final String channel = entry.get("channel");

        if ("Telegram".equals(channel)) {
            streamDataBuilder.channel("T");
        } else if ("Symphony".equals(channel)) {
            streamDataBuilder.channel("S");
        } else if ("Fix".equals(channel)) {
            streamDataBuilder.channel("F");
        } else if ("Rest".equals(channel)) {
            streamDataBuilder.channel("R");
        }

        return streamDataBuilder.build();
    }

    @DataTableType
    public OrderStreamData defineOrderStreamData(Map<String, String> entry) {
        return OrderStreamData
                .builder()
                .symbol(entry.get("symbol"))
                .price(new BigDecimal(entry.get("price")))
                .side(entry.get("side"))
                .quantity(new Long(entry.get("quantity")))
                .originalQuantity(new Long(entry.get("originalQuantity")))
                .liveQuantity(new Long(entry.get("liveQuantity")))
                .executionQuantity(new Long(entry.get("executionQuantity")))
                .trader(entry.get("trader"))
                .orderType(entry.get("orderType"))
                .orderStatus(entry.get("orderStatus"))
                .build();
    }
}
