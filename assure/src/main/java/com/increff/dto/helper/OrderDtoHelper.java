package com.increff.dto.helper;

import com.increff.util.BasicDataUtil;
import model.form.OrderForm;
import model.form.OrderItemForm;
import pojo.OrderItemPojo;
import pojo.OrderPojo;
import util.OrderStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderDtoHelper {
    public static String validateOrderForm(OrderForm orderForm) {
        if (orderForm == null) {
            return "OrderForm can't be null";
        }
        StringBuilder errorMsg = new StringBuilder();
        List<String> errors = new ArrayList<>();
        if (orderForm.getClientId() == null || orderForm.getClientId() <= 0) {
            errors.add("ClientID can't be null or less than 1");
        }
        if (orderForm.getCustomerId() == null || orderForm.getCustomerId() <= 0) {
            errors.add("CustomerID can't be null or less than 1");
        }
        if (BasicDataUtil.isEmpty(orderForm.getChannelName())) {
            errors.add("Channel Name can't be empty");
        }
        if (orderForm.getOrderItemList() == null || orderForm.getOrderItemList().isEmpty()) {
            errors.add("OrderItem list can't be empty");
        }
        if (BasicDataUtil.isEmpty(orderForm.getChannelOrderId())) {
            errors.add("ChannelOrderID can't be empty");
        }
        if (!errors.isEmpty()) {
            errorMsg.append(errors.get(0));
            for (int i = 1; i < errors.size(); i++) {
                errorMsg.append(", ").append(errors.get(i));
            }
            errorMsg.append(".\n");
        }
        errors.clear();
        Set<Long> channelIDSet = new HashSet<>();
        int orderItem = 1;
        for (OrderItemForm orderItemForm : orderForm.getOrderItemList()) {
            List<String> rowErrors = validateOrderItemForm(orderItemForm);
            if (channelIDSet.contains(orderItemForm.getChannelSkuId())) {
                rowErrors.add("Duplicate channelSkuId");
            } else {
                channelIDSet.add(orderItemForm.getChannelSkuId());
            }
            if (!rowErrors.isEmpty()) {
                errorMsg.append("Order Item ").append(orderItem).append(": ").append(rowErrors.get(0));
                for (int i = 1; i < rowErrors.size(); i++) {
                    errorMsg.append(", ").append(rowErrors.get(i));
                }
                errorMsg.append(".\n");
            }
            orderItem++;
        }
        return errorMsg.toString();
    }

    private static List<String> validateOrderItemForm(OrderItemForm orderItemForm) {
        List<String> errors = new ArrayList<>();
        if (orderItemForm == null) {
            errors.add("OrderItem can't be null");
            return errors;
        }
        if (orderItemForm.getOrderedQuantity() == null || orderItemForm.getOrderedQuantity() < 1) {
            errors.add("quantity can't be null or less than 1");
        }
        if (orderItemForm.getChannelSkuId() == null || orderItemForm.getChannelSkuId() < 1) {
            errors.add("ClientSkuId can't be null or less than 1");
        }
        if (orderItemForm.getSellingPricePerUnit() == null) {
            errors.add("Selling Price can't be null");
        }
        return errors;
    }

    public static OrderPojo convertToOrderPojoAndFillOrderItemList(OrderForm orderForm, List<OrderItemPojo> orderItemPojoList, List<Long> channelSkuIds) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderStatus(OrderStatus.CREATED);
        orderPojo.setChannelOrderId(orderForm.getChannelOrderId());
        orderPojo.setClientId(orderForm.getClientId());
        orderPojo.setCustomerId(orderForm.getCustomerId());
        for (OrderItemForm orderItemForm : orderForm.getOrderItemList()) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setOrderedQuantity(orderItemForm.getOrderedQuantity());
            orderItemPojo.setFulfilledQuantity(0L);
            orderItemPojo.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());
            channelSkuIds.add(orderItemForm.getChannelSkuId());
            orderItemPojoList.add(orderItemPojo);
        }
        return orderPojo;
    }

    public static String validateIdAndStatus(Long id, OrderStatus orderStatus) {
        StringBuilder errorMsg = new StringBuilder();
        if (id == null || id <= 0) {
            errorMsg.append("Id can't be null or less than 1");
        }
        if (orderStatus != OrderStatus.ALLOCATED && orderStatus != OrderStatus.FULFILLED) {
            if (errorMsg.length() != 0) errorMsg.append(", ");
            errorMsg.append("OrderStatus has to be either allocated or fulfilled");
        }
        return errorMsg.toString();
    }
}
