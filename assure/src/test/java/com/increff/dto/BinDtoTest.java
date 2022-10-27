package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.model.data.BinData;
import com.increff.model.forms.BinwiseInventoryForm;
import com.increff.model.forms.ProductForm;
import com.increff.model.forms.UserForm;
import com.increff.pojo.BinPojo;
import com.increff.pojo.ProductPojo;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class BinDtoTest extends AbstractUnitTest {
    @Autowired
    private BinDto binDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private UserDto userDto;

    @Test
    public void addTestLessThanOne() {
        try {
            binDto.add(0L);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Bin count cannot be less than 1");
        }
    }

    @Test
    public void addAndGetTestValid() throws ApiException {
        List<BinPojo> binPojoList = binDto.add(1L);
        List<BinData> binDataList = binDto.getAll();
        Assert.assertNotEquals(binDataList, null);
        Assert.assertNotEquals(binPojoList, null);
        Assert.assertEquals(binDataList.size(), 1);
        Assert.assertEquals(binPojoList.size(), 1);
        Assert.assertEquals(binDataList.get(0).getId(), binPojoList.get(0).getId());
    }

    @Test
    public void addInventoryTestInvalid() {
        List<BinwiseInventoryForm> inventoryFormList = new ArrayList<>();
        inventoryFormList.add(null);
        BinwiseInventoryForm nullFieldForm = new BinwiseInventoryForm();
        nullFieldForm.setBinId(null);
        nullFieldForm.setClientSkuId(null);
        nullFieldForm.setQuantity(null);
        inventoryFormList.add(nullFieldForm);
        try {
            BinwiseInventoryForm invalidQuantityForm = new BinwiseInventoryForm();
            invalidQuantityForm.setBinId(1L);
            invalidQuantityForm.setClientSkuId("s");
            invalidQuantityForm.setQuantity(0L);
            inventoryFormList.add(invalidQuantityForm);
            binDto.addInventory(null, inventoryFormList);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Client ID cannot be null.\nrow 1: Form cannot be null.\nrow 2: Bin Id cannot be null, ClientSkuId cannot be null or empty, Quantity cannot be null.\nrow 3: Quantity cannot be less than 1.\n");
        }
    }

    @Test
    public void addInventoryValid() throws ApiException {
        ProductForm productForm = new ProductForm();
        productForm.setName(" P ");
        productForm.setBrandId("b");
        productForm.setMrp(100.0d);
        productForm.setDescription("Desc");
        productForm.setClientSkuId("shoe229");
        UserForm userForm = new UserForm();
        userForm.setName("n");
        userForm.setType(UserType.CLIENT);
        UserPojo userPojo = userDto.add(userForm);
        productForm.setClientId(userPojo.getId());
        ProductPojo productPojo = productDto.add(List.of(productForm)).get(0);
        BinPojo binPojo = binDto.add(1L).get(0);
        BinwiseInventoryForm inventoryForm = new BinwiseInventoryForm();
        inventoryForm.setClientSkuId(productPojo.getClientSkuId());
        inventoryForm.setBinId(binPojo.getId());
        inventoryForm.setQuantity(1L);
        binDto.addInventory(1L, List.of(inventoryForm));
    }

}
