package com.increff.service;

import com.increff.config.AbstractUnitTest;
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
public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Test
    public void updateTestInvalidId() {
        ProductPojo productPojo = new ProductPojo();
        try {
            productService.update(productPojo);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Product doesn't exists");
        }
    }

    @Test
    public void updateTestInvalidClientAndClientSkus() throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<>();
        ProductPojo invalidClientPojo = new ProductPojo();
        invalidClientPojo.setClientId(null);
        invalidClientPojo.setBrandId("b");
        invalidClientPojo.setMrp(100d);
        invalidClientPojo.setDescription("Desc");
        invalidClientPojo.setName("p");
        productPojoList.add(invalidClientPojo);

        UserPojo userPojo = new UserPojo();
        userPojo.setType(UserType.CLIENT);
        userPojo.setName("u");
        userService.add(userPojo);

        ProductPojo validPojo = new ProductPojo();
        validPojo.setClientSkuId("sku");
        validPojo.setClientId(userPojo.getId());
        validPojo.setName("v");
        validPojo.setMrp(100d);
        validPojo.setBrandId("b");
        validPojo.setDescription("des");
        productPojoList.add(validPojo);

        ProductPojo invalidClientClientSkuPojo = new ProductPojo();
        invalidClientClientSkuPojo.setClientId(userPojo.getId());
        invalidClientClientSkuPojo.setClientSkuId("sku");
        invalidClientClientSkuPojo.setName("any");
        invalidClientClientSkuPojo.setMrp(100d);
        invalidClientClientSkuPojo.setBrandId("b");
        invalidClientClientSkuPojo.setDescription("des");
        productPojoList.add(invalidClientClientSkuPojo);

        try {
            productService.add(productPojoList);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Invalid Client Id.\nrow 3: ClientId and ClientSkuId combination already exists.\n");
        }
    }
}
