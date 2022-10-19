package pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_order_items")
public class OrderItemPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "global_sku_id", nullable = false)
    private Long globalSkuId;

    @Column(name = "ordered_quantity", nullable = false)
    private Long orderedQuantity;

    @Column(name = "allocated_quantity", nullable = false)
    private Long allocatedQuantity;

    @Column(name = "fulfilled_quantity", nullable = false)
    private Long fulfilledQuantity;

    @Column(name = "selling_price_per_unit", nullable = false)
    private Double sellingPricePerUnit;
}
