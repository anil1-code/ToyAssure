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
            return "OrderForm cannot be null";
        }
        StringBuilder errorMsg = new StringBuilder();
        List<String> errors = new ArrayList<>();
        if (orderForm.getClientId() == null) {
            errors.add("ClientID cannot be null");
        }
        if (orderForm.getCustomerId() == null) {
            errors.add("CustomerID cannot be null");
        }
        if (BasicDataUtil.isEmpty(orderForm.getChannelName())) {
            errors.add("Channel Name cannot be null or empty");
        }
        if (orderForm.getOrderItemList() == null) {
            errors.add("OrderItem list cannot be null");
        } else if (orderForm.getOrderItemList().isEmpty()) {
            errors.add("OrderItem list cannot be empty");
        }
        if (BasicDataUtil.isEmpty(orderForm.getChannelOrderId())) {
            errors.add("ChannelOrderID cannot be null or empty");
        }
        if (!errors.isEmpty()) {
            errorMsg.append(errors.get(0));
            for (int i = 1; i < errors.size(); i++) {
                errorMsg.append(", ").append(errors.get(i));
            }
            errorMsg.append(".\n");
        }
        errors.clear();
        if (orderForm.getOrderItemList() != null) {
            Set<String> channelIDSet = new HashSet<>();
            int orderItem = 1;
            for (OrderItemForm orderItemForm : orderForm.getOrderItemList()) {
                List<String> rowErrors = validateOrderItemForm(orderItemForm);
                if (orderItemForm != null) {
                    if (channelIDSet.contains(orderItemForm.getChannelSkuId())) {
                        rowErrors.add("Duplicate channelSkuId");
                    } else {
                        channelIDSet.add(orderItemForm.getChannelSkuId());
                    }
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
        }
        return errorMsg.toString();
    }

    private static List<String> validateOrderItemForm(OrderItemForm orderItemForm) {
        List<String> errors = new ArrayList<>();
        if (orderItemForm == null) {
            errors.add("OrderItem cannot be null");
            return errors;
        }
        if (orderItemForm.getOrderedQuantity() == null) {
            errors.add("quantity cannot be null");
        } else if (orderItemForm.getOrderedQuantity() < 1) {
            errors.add("quantity cannot be less than 1");
        }
        if (BasicDataUtil.isEmpty(orderItemForm.getChannelSkuId())) {
            errors.add("ChannelSkuId cannot be null or empty");
        }
        if (orderItemForm.getSellingPricePerUnit() == null) {
            errors.add("Selling Price cannot be null");
        } else if (orderItemForm.getSellingPricePerUnit() < 0) {
            errors.add("Selling Price cannot be negative");
        }
        return errors;
    }

    public static OrderPojo convertToOrderPojoAndFillOrderItemList(OrderForm orderForm, List<OrderItemPojo> orderItemPojoList, List<String> channelSkuIds) {
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
        if (id == null) {
            errorMsg.append("Id cannot be null");
        }
        if (orderStatus != OrderStatus.ALLOCATED && orderStatus != OrderStatus.FULFILLED) {
            if (errorMsg.length() != 0) errorMsg.append(", ");
            errorMsg.append("OrderStatus has to be either allocated or fulfilled");
        }
        if (errorMsg.length() != 0) errorMsg.append(".\n");
        return errorMsg.toString();
    }
}
