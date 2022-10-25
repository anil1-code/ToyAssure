package com.increff.dto;

import com.increff.config.AbstractUnitTest;
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

    @Test(expected = ApiException.class)
    public void addTestNullFields() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setName(null);
        productForm.setBrandId(null);
        productForm.setMrp(null);
        productForm.setDescription(null);
        productForm.setClientSkuId(null);
        try {
            productForm.setClientId(null);
            productFormList.add(null);
            productFormList.add(productForm);
            productDto.add(productFormList);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Form should not be null.\nrow 2: Brand ID should not be empty, Client ID should not be empty, Product name should not be empty, ClientSkuId should not be empty, Product description should not be empty, Mrp should not be null.\n");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addTestEmptyFields() throws ApiException {
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setName("");
        productForm.setBrandId("");
        productForm.setMrp(-1d);
        productForm.setDescription("");
        productForm.setClientSkuId("");
        try {
            productForm.setClientId(null);
            productFormList.add(productForm);
            productDto.add(productFormList);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Brand ID should not be empty, Client ID should not be empty, Product name should not be empty, ClientSkuId should not be empty, Product description should not be empty, Mrp should be positive.\n");
            throw e;
        }
    }

    @Test
    public void addTestValidFields() {
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setName(" P ");
        productForm.setBrandId("b");
        productForm.setMrp(100.0d);
        productForm.setDescription("Desc");
        productForm.setClientSkuId("shoe229");
        try {
            UserForm userForm = new UserForm();
            userForm.setName("n");
            userForm.setType(UserType.CLIENT);
            UserPojo userPojo = userDto.add(userForm);
            productForm.setClientId(userPojo.getId());
            productFormList.add(productForm);
            List<ProductPojo> productPojoList = productDto.add(productFormList);
            Assert.assertNotEquals(productPojoList, null);
            Assert.assertEquals(productPojoList.size(), 1);
            Assert.assertEquals(productPojoList.get(0).getName(), "p");
            Assert.assertEquals(productPojoList.get(0).getBrandId(), "b");
            Assert.assertEquals(productPojoList.get(0).getMrp(), Double.valueOf(100.00d));
            Assert.assertEquals(productPojoList.get(0).getDescription(), "Desc");
            Assert.assertEquals(productPojoList.get(0).getClientSkuId(), "shoe229");
            Assert.assertEquals(productPojoList.get(0).getClientId(), userPojo.getId());
        } catch (ApiException e) {
            Assert.fail();
        }
    }

//    @Test(expected = ApiException.class)
//    public void updateTestInvalidGlobalSkuId() throws ApiException {
//        ProduF
//        try {
//            productDto.update(0L, null);
//        } catch (ApiException e) {
//            Assert.assertEquals(e.getMessage(), "Product doesn't exists");
//            throw e;
//        }
//    }

    @Test
    public void updateTestValidFields() {
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = new ProductForm();
        productForm.setName(" P ");
        productForm.setBrandId("b");
        productForm.setMrp(100.0d);
        productForm.setDescription("Desc");
        productForm.setClientSkuId("shoe229");
        try {
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
        } catch (ApiException e) {
            Assert.fail();
        }
    }

}
