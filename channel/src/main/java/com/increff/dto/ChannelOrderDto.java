package com.increff.dto;

import exception.ApiException;
import model.data.InvoiceData;
import model.form.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pojo.OrderPojo;
import util.PDFMaker;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ChannelOrderDto {
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${server.Uri}")
    private String serverUri;

    public OrderPojo pushOrder(OrderForm orderForm) throws ApiException {
        try {
            return restTemplate.postForObject(serverUri + "orders", orderForm, OrderPojo.class);
        } catch (RestClientException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public byte[] generateInvoice(InvoiceData invoiceData) throws ApiException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss z");
        invoiceData.setInvoiceTime(ZonedDateTime.now().format(formatter));
        return PDFMaker.makePdf(invoiceData);
    }
}
