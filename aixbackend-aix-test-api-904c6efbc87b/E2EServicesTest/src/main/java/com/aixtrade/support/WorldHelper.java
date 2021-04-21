package com.aixtrade.support;


import com.aixtrade.orderControl.OrderIdControl;
import com.aixtrade.quoteControl.QuoteIdControl;
import com.aixtrade.quoteControl.QuoteRequestIdControl;
import com.aixtrade.quoteControl.QuoteResponseIdControl;
import com.aixtrade.httpsVerbs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorldHelper {

//    private static BaseVerb baseVerb;
    private final GetVerb getVerb;
    private final PostVerb postVerb;
    private final DeleteVerb deleteVerb;
    private final OrderIdControl getOrderIdControl;
    private final QuoteIdControl getQuoteIdControl;
    private final QuoteRequestIdControl getQuoteRequestIdControl;
    private final QuoteResponseIdControl getQuoteResponseIdControl;
//    private final QuoteIdControl getTraderIdControl;

    public GetVerb getVerb() {
//        if (getVerb != null) return getVerb;
//        getVerb = new GetVerb();
//        return getVerb();
        return getVerb;
    }

//    public BaseVerb baseVerb() {
//        if (baseVerb != null) return baseVerb;
//        baseVerb = new BaseVerb();
//        return baseVerb();
//    }

    public PostVerb postVerb() {
//        if (postVerb != null) return postVerb;
//        postVerb = new PostVerb();
//        return postVerb();
        return postVerb;
    }

    public DeleteVerb deleteVerb() {
//        if (deleteVerb != null) return deleteVerb;
//        deleteVerb = new DeleteVerb();
//        return deleteVerb();
        return deleteVerb;
    }

    public OrderIdControl getOrderIdControl() {
//        if (getOrderIdControl != null) return getOrderIdControl;
//        getOrderIdControl = new OrderIdControl();
//        return getOrderIdControl;
        return getOrderIdControl;
    }

    public QuoteIdControl getQuoteIdControl() {
//        if (getQuoteIdControl != null) return getQuoteIdControl;
//        getQuoteIdControl = new QuoteIdControl();
//        return getQuoteIdControl;
        return getQuoteIdControl;
    }

    public QuoteRequestIdControl getQuoteRequestIdControl() {
//        if (getQuoteRequestIdControl != null) return getQuoteRequestIdControl;
//        getQuoteRequestIdControl = new QuoteRequestIdControl();
//        return getQuoteRequestIdControl;
        return getQuoteRequestIdControl;
    }

    public QuoteResponseIdControl getQuoteResponseIdControl() {
//        if (getQuoteResponseIdControl != null) return getQuoteResponseIdControl;
//        getQuoteResponseIdControl = new QuoteResponseIdControl();
//        return getQuoteResponseIdControl;
        return getQuoteResponseIdControl;
    }

//    public QuoteIdControl getTraderIdControl() {
//        if (getTraderIdControl != null) return getTraderIdControl;
//        getTraderIdControl = new QuoteIdControl();
//        return getTraderIdControl;
//    }
}
