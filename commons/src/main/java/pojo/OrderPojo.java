package pojo;

import lombok.Getter;
import lombok.Setter;
import util.OrderStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_orders")
public class OrderPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @Column(name = "channel_order_id", nullable = false)
    private String channelOrderId;

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
}
