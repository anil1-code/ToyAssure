package com.increff.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_inventory", uniqueConstraints = {@UniqueConstraint(columnNames = {"globalSkuId"})})
public class InventoryPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long id;
    @NotNull
    private Long globalSkuId;
    @NotNull
    private Long availableQuantity;
    @NotNull
    private Long allocatedQuantity;
    @NotNull
    private Long fulfilledQuantity;
}
