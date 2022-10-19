package model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemInvoiceData {
    private String productName;
    private Long quantity;
    private Double sellingPricePerUnit;
    private Double sellingPrice;
}
