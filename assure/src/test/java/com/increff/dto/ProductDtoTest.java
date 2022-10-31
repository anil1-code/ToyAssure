package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.constants.Const;
import com.increff.model.data.ProductData;
import com.increff.model.forms.ProductForm;
import com.increff.model.forms.UserForm;
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
public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    private ProductDto productDto;
    @Autowired
    private UserDto userDto;

    @Test
    public void addTestNullList() {
        try {
            productDto.add(null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Product List cannot be null");
        }
    }

    @Test
    public void addTestNullFields() {
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setName(null);
        productForm.setBrandId(null);
        productForm.setMrp(null);
        productForm.setDescription(null);
        productForm.setClientSkuId(null);
        productForm.setClientId(null);
        productFormList.add(null);
        productFormList.add(productForm);
        try {
            productDto.add(productFormList);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Form cannot be null.\nrow 2: Brand ID cannot be null or empty, Client ID cannot be null, Product name cannot be null or empty, ClientSkuId cannot be null or empty, Mrp cannot be null.\n");
        }
    }

    @Test
    public void addTestEmptyFields() {
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setName("");
        productForm.setBrandId("");
        productForm.setMrp(-1d);
        productForm.setDescription("");
        productForm.setClientSkuId("");
        productForm.setClientId(null);
        productFormList.add(productForm);
        try {
            productDto.add(productFormList);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Brand ID cannot be null or empty, Client ID cannot be null, Product name cannot be null or empty, ClientSkuId cannot be null or empty, Mrp must be positive.\n");
        }
    }

    @Test
    public void addTestLargeFields() {
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setName("c".repeat(Const.MAX_LENGTH + 1));
        productForm.setBrandId("b".repeat(Const.MAX_LENGTH + 1));
        productForm.setMrp(100d);
        productForm.setDescription("d".repeat(Const.MAX_DESCRIPTION_LENGTH + 1));
        productForm.setClientSkuId("k".repeat(Const.MAX_LENGTH + 1));
        productForm.setClientId(1L);
        productFormList.add(productForm);
        try {
            productDto.add(productFormList);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Brand ID cannot exceed " + Const.MAX_LENGTH + " chars, Product name cannot exceed " + Const.MAX_LENGTH + " chars, ClientSkuId cannot exceed " + Const.MAX_LENGTH + " chars, Product description cannot exceed " + Const.MAX_DESCRIPTION_LENGTH + " chars.\n");
        }
    }

    @Test
    public void addAndGetTestValidFields() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
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
        productFormList.add(productForm);
        productDto.add(productFormList);
        List<ProductData> productDataList = productDto.getAll();
        Assert.assertNotEquals(productDataList, null);
        Assert.assertEquals(productDataList.size(), 1);
        Assert.assertEquals(productDataList.get(0).getName(), "p");
        Assert.assertEquals(productDataList.get(0).getBrandId(), "b");
        Assert.assertEquals(productDataList.get(0).getMrp(), Double.valueOf(100.00d));
        Assert.assertEquals(productDataList.get(0).getDescription(), "Desc");
        Assert.assertEquals(productDataList.get(0).getClientSkuId(), "shoe229");
        Assert.assertEquals(productDataList.get(0).getClientId(), userPojo.getId());
    }

    @Test
    public void updateTestInvalid() {
        try {
            productDto.update(null, null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Form cannot be null.\n");
        }
    }

    @Test
    public void updateTestValidFields() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
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
        productFormList.add(productForm);
        List<ProductPojo> productPojoList = productDto.add(productFormList);
        productForm.setName(" r ");
        productForm.setBrandId("bid");
        productForm.setMrp(500.0d);
        productForm.setDescription("Description");
        productDto.update(productPojoList.get(0).getGlobalSkuId(), productForm);
        List<ProductData> productDataList = productDto.getAll();
        Assert.assertNotEquals(productDataList, null);
        Assert.assertEquals(productDataList.size(), 1);
        Assert.assertEquals(productDataList.get(0).getName(), "r");
        Assert.assertEquals(productDataList.get(0).getBrandId(), "bid");
        Assert.assertEquals(productDataList.get(0).getMrp(), Double.valueOf(500.00d));
        Assert.assertEquals(productDataList.get(0).getDescription(), "Description");
    }

}