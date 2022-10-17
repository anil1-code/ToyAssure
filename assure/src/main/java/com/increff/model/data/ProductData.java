package com.increff.model.data;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData {
    @NotNull
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
