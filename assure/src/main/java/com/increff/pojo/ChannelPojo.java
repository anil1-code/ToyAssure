package com.increff.pojo;

import com.increff.util.InvoiceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pojo.AbstractPojo;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "assure_channels", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class ChannelPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "invoice_type", nullable = false)
    private InvoiceType invoiceType;
}
