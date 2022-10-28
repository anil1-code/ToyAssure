package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.model.forms.*;
import com.increff.pojo.BinPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.ProductPojo;
import com.increff.pojo.UserPojo;
import com.increff.util.InvoiceType;
import com.increff.util.UserType;
import exception.ApiException;
import model.form.OrderForm;
import model.form.OrderItemForm;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pojo.OrderPojo;
import util.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private UserDto userDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private ChannelDto channelDto;
    @Autowired
    private BinDto binDto;

    @Test
    public void createOrderTestNullForm() {
        try {
            orderDto.createOrder(null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "OrderForm cannot be null");
        }
    }

    @Test
    public void createOrderTestNullFields() {
        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(null);
        orderForm.setCustomerId(null);
        orderForm.setChannelName(null);
        orderForm.setOrderItemList(null);
        orderForm.setChannelOrderId(null);
        try {
            orderDto.createOrder(orderForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "ClientID cannot be null, CustomerID cannot be null, Channel Name cannot be null or empty, OrderItem list cannot be null, ChannelOrderID cannot be null or empty.\n");
        }
    }

    @Test
    public void createOrderTestEmptyFields() {
        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(0L);
        orderForm.setCustomerId(0L);
        orderForm.setChannelName("");
        orderForm.setOrderItemList(new ArrayList<>());
        orderForm.setChannelOrderId("");
        try {
            orderDto.createOrder(orderForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel Name cannot be null or empty, OrderItem list cannot be empty, ChannelOrderID cannot be null or empty.\n");
        }
    }

    @Test
    public void createOrderTestDuplicateSkus() {
        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(0L);
        orderForm.setCustomerId(0L);
        orderForm.setChannelName("");
        orderForm.setChannelOrderId("");
        OrderItemForm orderItemForm1 = new OrderItemForm();
        orderItemForm1.setOrderedQuantity(null);
        orderItemForm1.setChannelSkuId("channel sku");
        orderItemForm1.setSellingPricePerUnit(null);
        OrderItemForm orderItemForm2 = new OrderItemForm();
        orderItemForm2.setOrderedQuantity(0L);
        orderItemForm2.setChannelSkuId("channel sku");
        orderItemForm2.setSellingPricePerUnit(-1d);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(null);
        orderItemFormList.add(orderItemForm1);
        orderItemFormList.add(orderItemForm2);
        orderForm.setOrderItemList(orderItemFormList);
        try {
            orderDto.createOrder(orderForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel Name cannot be null or empty, ChannelOrderID cannot be null or empty.\nOrder Item 1: OrderItem cannot be null.\nOrder Item 2: quantity cannot be null, Selling Price cannot be null.\nOrder Item 3: quantity cannot be less than 1, Selling Price cannot be negative, Duplicate channelSkuId.\n");
        }
    }

    @Test
    public void createOrderTestNullAndEmptyChannelSku() {
        OrderItemForm orderItemForm1 = new OrderItemForm();
        orderItemForm1.setOrderedQuantity(1L);
        orderItemForm1.setChannelSkuId(null);
        orderItemForm1.setSellingPricePerUnit(1d);

        OrderItemForm orderItemForm2 = new OrderItemForm();
        orderItemForm2.setOrderedQuantity(1L);
        orderItemForm2.setChannelSkuId("");
        orderItemForm2.setSellingPricePerUnit(1d);

        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(1L);
        orderForm.setCustomerId(1L);
        orderForm.setChannelName("channel");
        orderForm.setChannelOrderId("channel order id");
        orderForm.setOrderItemList(List.of(orderItemForm1, orderItemForm2));
        try {
            orderDto.createOrder(orderForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Order Item 1: ChannelSkuId cannot be null or empty.\nOrder Item 2: ChannelSkuId cannot be null or empty.\n");
        }
    }

    @Test
    public void createOrderTestValidFields() throws ApiException {
        ChannelPojo channelPojo = createChannel("c");
        UserPojo clientPojo = createClient();
        ProductPojo productPojo = createProduct(clientPojo);

        ChannelIDMapForm channelIDMapForm = new ChannelIDMapForm();
        channelIDMapForm.setClientSkuId(productPojo.getClientSkuId());
        channelIDMapForm.setChannelSkuId("channel sku id");

        channelDto.addChannelIDMappings(clientPojo.getName(), channelPojo.getName(), List.of(channelIDMapForm));

        UserPojo customerPojo = createCustomer();

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setChannelSkuId("channel sku id");
        orderItemForm.setOrderedQuantity(1L);
        orderItemForm.setSellingPricePerUnit(100d);

        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(clientPojo.getId());
        orderForm.setCustomerId(customerPojo.getId());
        orderForm.setChannelName(channelPojo.getName());
        orderForm.setChannelOrderId("channel order id");
        orderForm.setOrderItemList(List.of(orderItemForm));

        orderDto.createOrder(orderForm);
    }

    @Test
    public void createOrderTestInternalOrderAndDuplicateChannelOrderId() throws ApiException {
        ChannelPojo channelPojo = createChannel("INTERNAL");
        UserPojo clientPojo = createClient();
        ProductPojo productPojo = createProduct(clientPojo);

        UserPojo customerPojo = createCustomer();

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setChannelSkuId(productPojo.getClientSkuId());
        orderItemForm.setOrderedQuantity(1L);
        orderItemForm.setSellingPricePerUnit(100d);

        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(clientPojo.getId());
        orderForm.setCustomerId(customerPojo.getId());
        orderForm.setChannelName(channelPojo.getName());
        orderForm.setChannelOrderId("channel order id");
        orderForm.setOrderItemList(List.of(orderItemForm));

        orderDto.createOrder(orderForm);
        try {
            orderDto.createOrder(orderForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel OrderID already exists for this client and channel");
        }
    }

    @Test
    public void testInvalidAllocateAndFullFillAndInvoice() throws ApiException {
        try {
            orderDto.changeStatus(1L, OrderStatus.ALLOCATED);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "No Order exists with the given ID: 1");
        }
        try {
            orderDto.changeStatus(1L, OrderStatus.FULFILLED);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "No Order exists with the given ID: 1");
        }
        try {
            orderDto.generateInvoice(1L);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "No Order exists with the given ID: 1");
        }


        ChannelPojo channelPojo = createChannel("INTERNAL");
        UserPojo clientPojo = createClient();
        ProductPojo productPojo = createProduct(clientPojo);

        UserPojo customerPojo = createCustomer();

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setChannelSkuId(productPojo.getClientSkuId());
        orderItemForm.setOrderedQuantity(1L);
        orderItemForm.setSellingPricePerUnit(100d);

        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(clientPojo.getId());
        orderForm.setCustomerId(customerPojo.getId());
        orderForm.setChannelName(channelPojo.getName());
        orderForm.setChannelOrderId("channel order id");
        orderForm.setOrderItemList(List.of(orderItemForm));

        OrderPojo orderPojo = orderDto.createOrder(orderForm);
        orderDto.changeStatus(orderPojo.getId(), OrderStatus.ALLOCATED); // since inventory is not added, order status will not change

        try {
            orderDto.changeStatus(orderPojo.getId(), OrderStatus.FULFILLED);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Order status should be ALLOCATED");
        }
        try {
            orderDto.generateInvoice(orderPojo.getId());
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Order isn't fulfilled yet");
        }
    }

    @Test
    public void changeStatusTestNullInputs() {
        try {
            orderDto.changeStatus(null, null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Id cannot be null, OrderStatus has to be either allocated or fulfilled.\n");
        }
    }

    @Test
    public void changeStatusAndGenerateInvoiceTestValid() throws ApiException {
        ChannelPojo channelPojo = createChannel("c");
        UserPojo clientPojo = createClient();
        ProductPojo productPojo = createProduct(clientPojo);

        ChannelIDMapForm channelIDMapForm = new ChannelIDMapForm();
        channelIDMapForm.setClientSkuId(productPojo.getClientSkuId());
        channelIDMapForm.setChannelSkuId("channel sku id");

        channelDto.addChannelIDMappings(clientPojo.getName(), channelPojo.getName(), List.of(channelIDMapForm));

        UserPojo customerPojo = createCustomer();

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setChannelSkuId("channel sku id");
        orderItemForm.setOrderedQuantity(1L);
        orderItemForm.setSellingPricePerUnit(100d);

        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(clientPojo.getId());
        orderForm.setCustomerId(customerPojo.getId());
        orderForm.setChannelName(channelPojo.getName());
        orderForm.setChannelOrderId("channel order id");
        orderForm.setOrderItemList(List.of(orderItemForm));

        List<BinPojo> binPojoList = binDto.add(1L);
        BinwiseInventoryForm binwiseInventoryForm = new BinwiseInventoryForm();
        binwiseInventoryForm.setClientSkuId(productPojo.getClientSkuId());
        binwiseInventoryForm.setQuantity(1L);
        binwiseInventoryForm.setBinId(binPojoList.get(0).getId());
        binDto.addInventory(clientPojo.getId(), List.of(binwiseInventoryForm));

        OrderPojo orderPojo = orderDto.createOrder(orderForm);
        orderDto.changeStatus(orderPojo.getId(), OrderStatus.ALLOCATED);
        orderDto.changeStatus(orderPojo.getId(), OrderStatus.FULFILLED);
        orderDto.generateInvoice(orderPojo.getId());
    }

    @Test
    public void generateInvoiceTestNullInput() {
        try {
            orderDto.generateInvoice(null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Order ID cannot be null");
        }
    }

    private UserPojo createClient() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName("temp client");
        userForm.setType(UserType.CLIENT);
        return userDto.add(userForm);
    }

    private UserPojo createCustomer() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName("temp customer");
        userForm.setType(UserType.CUSTOMER);
        return userDto.add(userForm);
    }

    private ProductPojo createProduct(UserPojo userPojo) throws ApiException {
        ProductForm productForm = new ProductForm();
        productForm.setMrp(100d);
        productForm.setDescription("desc");
        productForm.setName("p name");
        productForm.setBrandId("bid");
        productForm.setClientSkuId("c sku id");
        productForm.setClientId(userPojo.getId());
        return productDto.add(List.of(productForm)).get(0);
    }

    private ChannelPojo createChannel(String name) throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName(name);
        channelForm.setInvoiceType(InvoiceType.SELF);
        return channelDto.add(channelForm);
    }
}
