package com.increff.service;

import com.increff.config.AbstractUnitTest;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pojo.OrderItemPojo;
import pojo.OrderPojo;
import util.OrderStatus;

import java.util.List;

@Transactional
public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void createOrderTestInvalidChannelClientCustomerId() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId("1");
        orderPojo.setClientId(1L);
        orderPojo.setCustomerId(1L);
        orderPojo.setOrderStatus(OrderStatus.CREATED);
        try {
            orderService.createOrder(orderPojo, List.of(new OrderItemPojo()), List.of("sku"), "some channel name");
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel doesn't not exist, Client doesn't exist, Customer doesn't existOrderItem 1: No globalSkuId exists for the given channel, client and channelSkuId.\n");
        }
    }
}
