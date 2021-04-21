package com.aixtrade.orderControl;

import com.aixtrade.httpsVerbs.DeleteVerb;
import com.aixtrade.utilities.EnvConfig;
import com.aixtrade.utilities.TestData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderIdControl {

    public final List<String> orderIds = new CopyOnWriteArrayList<>();

    @Getter
    private String orderId;

    private final DeleteVerb deleteVerb;

    public String storeOrderId(String newOrderId) {
        orderIds.add(newOrderId);
        orderId = newOrderId;
        return newOrderId;
    }

    public void deleteOrderIds() {
        log.debug("Found order ids" + orderIds);
        orderIds.forEach(id -> {
            String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
            String orderUri = TestData.getValue("orderService uri");
            String endpoint = orderBaseUrl + orderUri + "/" + id;
//            DeleteVerb deleteVerb = new DeleteVerb();
             deleteVerb.deleteOnApi(endpoint, null);
        });
    }

    public void deleteTwoWayOrderIds() {
        log.debug("Found order ids" + orderIds);
        orderIds.forEach(id -> {
            String orderBaseUrl = EnvConfig.getValue("orderService baseUrl");
            String orderUri = TestData.getValue("cancelTwoWayOrder uri");
            String endpoint = orderBaseUrl + orderUri + "/" + id;
//            DeleteVerb deleteVerb = new DeleteVerb();
            deleteVerb.deleteOnApi(endpoint, null);
        });
    }

    public void resetOrderIds() {
        orderIds.clear();
    }
}
