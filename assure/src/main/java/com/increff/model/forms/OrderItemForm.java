package com.increff.model.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private Long channelSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
