package com.increff.pojo;

import lombok.Getter;
import lombok.Setter;
import pojo.AbstractPojo;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_inventory", uniqueConstraints = {@UniqueConstraint(columnNames = {"global_sku_id"})})
public class InventoryPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "global_sku_id", nullable = false)
    private Long globalSkuId;

    @Column(name = "available_quantity", nullable = false)
    private Long availableQuantity;

    @Column(name = "allocated_quantity", nullable = false)
    private Long allocatedQuantity;

    @Column(name = "fulfilled_quantity", nullable = false)
    private Long fulfilledQuantity;
}
