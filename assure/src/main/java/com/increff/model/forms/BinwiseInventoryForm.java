package com.increff.model.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinwiseInventoryForm {
    private Long binId;
    private String clientSkuId;
    private Long quantity;
}
