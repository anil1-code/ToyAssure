package com.increff.model.forms;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    private String clientSkuId;
    private Long clientId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}
