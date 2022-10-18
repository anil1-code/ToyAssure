package com.increff.service;

import com.increff.dao.OrderDao;
import com.increff.exception.ApiException;
import com.increff.pojo.*;
import com.increff.util.InvoiceType;
import com.increff.util.OrderStatus;
import com.increff.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;
    @Autowired
    private BinService binService;

    public void validate(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList, List<Long> channelSkuIds, String channelName) throws ApiException {
        ChannelPojo channelPojo = channelService.getByName(channelName);
        StringBuilder errorMsg = new StringBuilder();
        // check if channel name exists or not and set the channel id
        if (channelPojo == null) {
            errorMsg.append("Channel doesn't not exist");
        } else {
            orderPojo.setChannelId(channelPojo.getId());
            // check if channelOrderId is unique or not for the given client, channel combination
            if (orderDao.getByClientChannelAndChannelOrderId(orderPojo.getClientId(), channelPojo.getId(), orderPojo.getChannelOrderId()) != null) {
                if (errorMsg.length() != 0)
                    errorMsg.append(", ");
                errorMsg.append("Channel OrderID already exists for this client and channel");
            }
        }
        UserPojo clientPojo = userService.getById(orderPojo.getClientId());
        // check if client exists or not
        if (clientPojo == null || clientPojo.getType() != UserType.CLIENT) {
            if (errorMsg.length() != 0)
                errorMsg.append(", ");
            errorMsg.append("Client doesn't exist");
        }
        // check if customer exists or not
        UserPojo customerPojo = userService.getById(orderPojo.getCustomerId());
        if (customerPojo == null || customerPojo.getType() != UserType.CUSTOMER) {
            if (errorMsg.length() != 0)
                errorMsg.append(", ");
            errorMsg.append("Customer doesn't exist");
        }
        int orderItem = 1;
        // check if globalSkuIds exists or not for the given channelSkuIds and channelId combination
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            ChannelListingPojo channelListingPojo = null;
            if (clientPojo != null && channelPojo != null)
                channelListingPojo = channelService.getByClientChannelAndChannelSkuId(clientPojo.getId(), channelPojo.getId(), channelSkuIds.get(orderItem++));
            if (channelListingPojo == null) {
                errorMsg.append("OrderItem ").append(orderItem).append(": ").append("No globalSkuId exists for the given channel, client and channelSkuId.\n");
            } else {
                orderItemPojo.setGlobalSkuId(channelListingPojo.getGlobalSkuId());
            }
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo createOrder(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList, List<Long> channelSkuIds, String channelName) throws ApiException {
        validate(orderPojo, orderItemPojoList, channelSkuIds, channelName);
        OrderPojo createdPojo = orderDao.createOrder(orderPojo);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemPojo.setOrderId(createdPojo.getId());
            orderDao.createOrderItem(orderItemPojo);
        }
        return createdPojo;
    }


    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo allocateOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderDao.getById(id);
        if (orderPojo == null) {
            throw new ApiException("No Order exists with the given ID: " + id);
        }
        if (orderPojo.getOrderStatus() != OrderStatus.CREATED) {
            throw new ApiException("Order status should be CREATED");
        }
        List<OrderItemPojo> orderItemPojoList = orderDao.getOrderItemsByOrderId(id);
        boolean successful = true;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long allocated = binService.allocateBinSkus(orderItemPojo);
            if (!Objects.equals(orderItemPojo.getAllocatedQuantity(), orderItemPojo.getOrderedQuantity())) {
                successful = false;
            }
            binService.allocateInventory(orderItemPojo.getGlobalSkuId(), allocated);
        }
        if (successful) orderPojo.setOrderStatus(OrderStatus.ALLOCATED);
        return orderPojo;
    }


    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo fulfillOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderDao.getById(id);
        if (orderPojo == null) {
            throw new ApiException("No Order exists with the given ID: " + id);
        }
        if (orderPojo.getOrderStatus() != OrderStatus.ALLOCATED) {
            throw new ApiException("Order status should be ALLOCATED");
        }
        List<OrderItemPojo> orderItemPojoList = orderDao.getOrderItemsByOrderId(id);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            binService.fulfillInventory(orderItemPojo.getGlobalSkuId(), orderItemPojo.getAllocatedQuantity());
            orderItemPojo.setFulfilledQuantity(orderItemPojo.getAllocatedQuantity());
            orderItemPojo.setAllocatedQuantity(0L);
        }
        orderPojo.setOrderStatus(OrderStatus.FULFILLED);
        return orderPojo;
    }

    public void generateInvoice(Long id) throws ApiException {
        OrderPojo orderPojo = orderDao.getById(id);
        if (orderPojo == null) {
            throw new ApiException("No Order exists with the given ID: " + id);
        }
        if (orderPojo.getOrderStatus() != OrderStatus.FULFILLED) {
            throw new ApiException("Order isn't fulfilled yet");
        }
        ChannelPojo channelPojo = channelService.getById(orderPojo.getChannelId());
        if (channelPojo.getInvoiceType() == InvoiceType.CHANNEL) {
            // call channel api for this
            // TODO: 18/10/22 revisit after developing channel module
        }
        // generate here

    }
}
