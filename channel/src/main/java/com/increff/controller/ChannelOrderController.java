package com.increff.controller;

import com.increff.dto.ChannelOrderDto;
import exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.data.InvoiceData;
import model.form.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pojo.OrderPojo;

@Api
@RestController
@RequestMapping(value = "/api/orders")
public class ChannelOrderController {
    @Autowired
    private ChannelOrderDto channelOrderDto;

    @ApiOperation(value = "push the order via this channel")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public OrderPojo pushOrder(@RequestBody OrderForm orderForm) throws ApiException {
        return channelOrderDto.pushOrder(orderForm);
    }

    @ApiOperation(value = "get invoice")
    @RequestMapping(value = "/generate-invoice", method = RequestMethod.POST)
    public byte[] generateInvoice(@RequestBody InvoiceData invoiceData) throws ApiException {
        return channelOrderDto.generateInvoice(invoiceData);
    }

}
