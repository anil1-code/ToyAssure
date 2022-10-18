package com.increff.controller;

import com.increff.dto.OrderDto;
import com.increff.exception.ApiException;
import com.increff.model.forms.OrderForm;
import com.increff.pojo.OrderPojo;
import com.increff.util.OrderStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
    public void generateInvoice(@PathVariable Long id, HttpServletResponse httpServletResponse) throws ApiException {
        orderDto.generateInvoice(id);
        // TODO: 18/10/22 to be developed...
    }


}
