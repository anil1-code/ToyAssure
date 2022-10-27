package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.model.forms.*;
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

    @Test(expected = ApiException.class)
    public void createOrderTestNullForm() throws ApiException {
        try {
            orderDto.createOrder(null);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "OrderForm can't be null");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void createOrderTestNullFields() throws ApiException {
        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(null);
        orderForm.setCustomerId(null);
        orderForm.setChannelName(null);
        orderForm.setOrderItemList(null);
        orderForm.setChannelOrderId(null);
        try {
            orderDto.createOrder(orderForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "ClientID can't be null, CustomerID can't be null, Channel Name can't be empty, OrderItem list can't be null, ChannelOrderID can't be empty.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void createOrderTestEmptyOrInvalidFields() throws ApiException {
        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(0L);
        orderForm.setCustomerId(0L);
        orderForm.setChannelName("");
        orderForm.setOrderItemList(new ArrayList<>());
        orderForm.setChannelOrderId("");
        try {
            orderDto.createOrder(orderForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "ClientID can't be less than 1, CustomerID can't be less than 1, Channel Name can't be empty, OrderItem list can't be empty, ChannelOrderID can't be empty.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void createOrderTestDuplicateSkus() throws ApiException {
        OrderForm orderForm = new OrderForm();
        orderForm.setClientId(0L);
        orderForm.setCustomerId(0L);
        orderForm.setChannelName("");
        orderForm.setChannelOrderId("");
        OrderItemForm orderItemForm1 = new OrderItemForm();
        orderItemForm1.setOrderedQuantity(null);
        orderItemForm1.setChannelSkuId("some sku");
        orderItemForm1.setSellingPricePerUnit(100d);
        OrderItemForm orderItemForm2 = new OrderItemForm();
        orderItemForm2.setOrderedQuantity(0L);
        orderItemForm2.setChannelSkuId("some sku");
        orderItemForm2.setSellingPricePerUnit(100d);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(null);
        orderItemFormList.add(orderItemForm1);
        orderItemFormList.add(orderItemForm2);
        orderForm.setOrderItemList(orderItemFormList);
        try {
            orderDto.createOrder(orderForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "ClientID can't be less than 1, CustomerID can't be less than 1, Channel Name can't be empty, ChannelOrderID can't be empty.\nOrder Item 1: OrderItem can't be null.\nOrder Item 2: quantity can't be null.\nOrder Item 3: quantity can't be less than 1, Duplicate channelSkuId.\n");
            throw e;
        }
    }

//    @Test
//    public void createOrderTestValidFields() throws ApiException {
//        ChannelPojo channelPojo = createChannel();
//        UserPojo clientPojo = createClient();
//        ProductPojo productPojo = createProduct(clientPojo);
//        ChannelIDMapForm channelIDMapForm = new ChannelIDMapForm();
//        channelIDMapForm.setClientSkuId(productPojo.getGlobalSkuId());
//        channelIDMapForm.setClientName(clientPojo.getName());
//        channelIDMapForm.setChannelName(channelPojo.getName());
//        channelIDMapForm.setChannelSkuId("channel sku id");
//        channelDto.addChannelIDMappings(List.of(channelIDMapForm));
//        UserPojo customerPojo = createCustomer();
//        OrderForm orderForm = new OrderForm();
//        orderForm.setClientId(clientPojo.getId());
//        orderForm.setCustomerId(customerPojo.getId());
//        orderForm.setChannelName(channelPojo.getName());
//        orderForm.setChannelOrderId("channel order id");
//        OrderItemForm orderItemForm = new OrderItemForm();
//        orderItemForm.setChannelSkuId("channel sku id");
//        orderItemForm.setOrderedQuantity(1L);
//        orderItemForm.setSellingPricePerUnit(100d);
//        orderForm.setOrderItemList(List.of(orderItemForm));
//        try {
//            orderDto.createOrder(orderForm);
//        } catch (ApiException e) {
//            Assert.fail();
//        }
//    }

    @Test(expected = ApiException.class)
    public void changeStatusTestNullInputs() throws ApiException {
        try {
            orderDto.changeStatus(null, null);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Id can't be null, OrderStatus has to be either allocated or fulfilled.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void changeStatusTestInvalidID() throws ApiException {
        try {
            orderDto.changeStatus(0L, null);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Id can't be less than 1, OrderStatus has to be either allocated or fulfilled.\n");
            throw e;
        }
    }

//    @Test
//    public void changeStatusAndGenerateInvoiceTestValid() {
//        try {
//            ChannelPojo channelPojo = createChannel();
//            UserPojo clientPojo = createClient();
//            ProductPojo productPojo = createProduct(clientPojo);
//            ChannelIDMapForm channelIDMapForm = new ChannelIDMapForm();
//            channelIDMapForm.setGlobalSkuId(productPojo.getGlobalSkuId());
//            channelIDMapForm.setClientName(clientPojo.getName());
//            channelIDMapForm.setChannelName(channelPojo.getName());
//            channelIDMapForm.setChannelSkuId("channel sku id");
//            channelDto.addChannelIDMappings(List.of(channelIDMapForm));
//            UserPojo customerPojo = createCustomer();
//            OrderForm orderForm = new OrderForm();
//            orderForm.setClientId(clientPojo.getId());
//            orderForm.setCustomerId(customerPojo.getId());
//            orderForm.setChannelName(channelPojo.getName());
//            orderForm.setChannelOrderId("channel order id");
//            OrderItemForm orderItemForm = new OrderItemForm();
//            orderItemForm.setChannelSkuId("channel sku id");
//            orderItemForm.setOrderedQuantity(1L);
//            orderItemForm.setSellingPricePerUnit(100d);
//            orderForm.setOrderItemList(List.of(orderItemForm));
//            List<BinPojo> binPojoList = binDto.add(1L);
//            BinwiseInventoryForm binwiseInventoryForm = new BinwiseInventoryForm();
//            binwiseInventoryForm.setClientSkuId(productPojo.getGlobalSkuId());
//            binwiseInventoryForm.setQuantity(1L);
//            binwiseInventoryForm.setBinId(binPojoList.get(0).getId());
//            binDto.addInventory(List.of(binwiseInventoryForm));
//            OrderPojo orderPojo = orderDto.createOrder(orderForm);
//            orderDto.changeStatus(orderPojo.getId(), OrderStatus.ALLOCATED);
//            orderDto.changeStatus(orderPojo.getId(), OrderStatus.FULFILLED);
//            orderDto.generateInvoice(orderPojo.getId());
//        } catch (ApiException e) {
//            Assert.fail();
//        }
//    }

    @Test(expected = ApiException.class)
    public void generateInvoiceTestNullInput() throws ApiException {
        try {
            orderDto.generateInvoice(null);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Order ID can't be null or less than 1");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void generateInvoiceTestInvalidInput() throws ApiException {
        try {
            orderDto.generateInvoice(0L);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Order ID can't be null or less than 1");
            throw e;
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

    private ChannelPojo createChannel() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName("C");
        channelForm.setInvoiceType(InvoiceType.SELF);
        return channelDto.add(channelForm);
    }
}
