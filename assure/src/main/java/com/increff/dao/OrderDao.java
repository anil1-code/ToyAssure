package com.increff.dao;

import com.increff.pojo.OrderItemPojo;
import com.increff.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {


    public OrderPojo createOrder(OrderPojo orderPojo) {
        em().persist(orderPojo);
        return orderPojo;
    }

    public OrderItemPojo createOrderItem(OrderItemPojo orderItemPojo) {
        em().persist(orderItemPojo);
        return orderItemPojo;
    }

    private static final String selectByClientChannelAndChannelOrderId = "select p from OrderPojo p where clientId=:clientId and channelId=:channelId and channelOrderId=:channelOrderId";
    private static final String selectOrderById = "select p from OrderPojo p where id=:id";
    private static final String selectOrderItemsByOrderId = "select p from OrderItemPojo p where orderId=:orderId";


    public OrderPojo getByClientChannelAndChannelOrderId(Long clientId, Long channelId, String channelOrderId) {
        TypedQuery<OrderPojo> query = getQuery(selectByClientChannelAndChannelOrderId, OrderPojo.class);
        query.setParameter("clientId", clientId);
        query.setParameter("channelId", channelId);
        query.setParameter("channelOrderId", channelOrderId);
        return getSingle(query);
    }

    public OrderPojo getById(Long id) {
        TypedQuery<OrderPojo> query = getQuery(selectOrderById, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Long orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(selectOrderItemsByOrderId, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
