package model.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderForm {
    private Long clientId;
    private Long customerId;
    private String channelName;
    private String channelOrderId;
    private List<OrderItemForm> orderItemList;
}
