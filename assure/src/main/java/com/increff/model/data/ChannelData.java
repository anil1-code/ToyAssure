package com.increff.model.data;

import com.increff.util.InvoiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelData {
    private Long id;
    private String name;
    private InvoiceType invoiceType;
}
