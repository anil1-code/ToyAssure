package com.increff.dto;

import com.increff.dto.helper.OrderDtoHelper;
import com.increff.service.OrderService;
import exception.ApiException;
import model.form.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pojo.OrderItemPojo;
import pojo.OrderPojo;
import util.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;

    public OrderPojo createOrder(OrderForm orderForm) throws ApiException {
        String error = OrderDtoHelper.validateOrderForm(orderForm);
        if (error.length() != 0) {
            throw new ApiException(error);
        }
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        List<String> channelSkuIds = new ArrayList<>();
        OrderPojo orderPojo = OrderDtoHelper.convertToOrderPojoAndFillOrderItemList(orderForm, orderItemPojoList, channelSkuIds);
        return orderService.createOrder(orderPojo, orderItemPojoList, channelSkuIds, orderForm.getChannelName());
    }

    public OrderPojo changeStatus(Long id, OrderStatus orderStatus) throws ApiException {
        String error = OrderDtoHelper.validateIdAndStatus(id, orderStatus);
        if (!error.isEmpty()) {
            throw new ApiException(error);
        }
        if (orderStatus == OrderStatus.ALLOCATED)
            return orderService.allocateOrder(id);
        return orderService.fulfillOrder(id);
    }

    public byte[] generateInvoice(Long id) throws ApiException {
        if (id == null || id <= 0) {
            throw new ApiException("Order ID can't be null or less than 1");
        }
        return orderService.generateInvoice(id);
    }
}
