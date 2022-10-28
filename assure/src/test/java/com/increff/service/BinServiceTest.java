package com.increff.service;

import com.increff.config.AbstractUnitTest;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class BinServiceTest extends AbstractUnitTest {
    @Autowired
    private BinService binService;
    @Autowired
    private UserService userService;

    @Test
    public void addInventoryInvalidClient() {
        try {
            binService.addInventory(1L, null, null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "No Client exists with given ID: 1");
        }
    }

    @Test
    public void addInventoryInvalid() throws ApiException {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(1L);
        UserPojo userPojo = createUser();
        try {
            binService.addInventory(userPojo.getId(), List.of(binSkuPojo), List.of("sku"));
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Bin doesn't exist, Product doesn't exist.\n");
        }
    }

    private UserPojo createUser() throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setName("c");
        userPojo.setType(UserType.CLIENT);
        return userService.add(userPojo);
    }
}
