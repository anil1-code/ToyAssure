package model.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private Long channelSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
