package model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceData {
    private String orderedTime;
    private String invoiceTime;
    private String channelOrderId;

    private String clientName;
    private String customerName;
    private String channelName;

    private Double total;

    private List<OrderItemInvoiceData> orderItemDataList;
}
