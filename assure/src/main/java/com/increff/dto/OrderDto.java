package com.increff.dto;

import com.increff.dto.helper.OrderDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.forms.OrderForm;
import com.increff.pojo.OrderItemPojo;
import com.increff.pojo.OrderPojo;
import com.increff.service.OrderService;
import com.increff.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        List<Long> channelSkuIds = new ArrayList<>();
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

    public void generateInvoice(Long id) throws ApiException {
        if(id == null || id <= 0) {
            throw new ApiException("Order ID can't be null or less than 1");
        }
        orderService.generateInvoice(id);
    }
}
