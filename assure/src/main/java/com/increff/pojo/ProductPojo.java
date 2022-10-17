package com.increff.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_products", uniqueConstraints = {@UniqueConstraint(columnNames = {"globalSkuId"}), @UniqueConstraint(columnNames = {"clientId", "clientSkuId"})})
public class ProductPojo extends AbstractPojo {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long globalSkuId;
    @NotNull
    private String clientSkuId;
    @NotNull
    private Long clientId;
    @NotNull
    private String name;
    @NotNull
    private String brandId;
    @NotNull
    private Double mrp;
    @NotNull
    private String description;
}
