package com.increff.controller;

import com.increff.dto.OrderDto;
import exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.form.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.OrderPojo;
import util.OrderStatus;

@Api
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "creates the order")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public OrderPojo createOrder(@RequestBody OrderForm orderForm) throws ApiException {
        return orderDto.createOrder(orderForm);
    }

    @ApiOperation(value = "allocate orders")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public OrderPojo changeStatus(@PathVariable Long id, @RequestBody OrderStatus orderStatus) throws ApiException {
        return orderDto.changeStatus(id, orderStatus);
    }

    @ApiOperation(value = "generate invoice for the specified order")
    @RequestMapping(value = "/generate-invoice/{id}", method = RequestMethod.GET)
    public byte[] generateInvoice(@PathVariable Long id) throws ApiException {
        return orderDto.generateInvoice(id);
    }


}
