package com.increff.pojo;

import lombok.Getter;
import lombok.Setter;
import pojo.AbstractPojo;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_products", uniqueConstraints = {@UniqueConstraint(columnNames = {"client_id", "client_sku_id"})})
public class ProductPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "global_sku_id", nullable = false)
    private Long globalSkuId;

    @Column(name = "client_sku_id", nullable = false)
    private String clientSkuId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand_id", nullable = false)
    private String brandId;

    @Column(name = "mrp", nullable = false)
    private Double mrp;

    @Column(name = "description", nullable = false)
    private String description;
}
