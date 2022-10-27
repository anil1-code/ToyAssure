package com.increff.service;

import com.increff.config.AbstractUnitTest;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceTest extends AbstractUnitTest {
    @Autowired
    private UserService userService;

    @Test
    public void addTestDuplicateCombination() throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setName("c");
        userPojo.setType(UserType.CLIENT);
        userService.add(userPojo);
        UserPojo duplicatePojo = new UserPojo();
        duplicatePojo.setName("c");
        duplicatePojo.setType(UserType.CLIENT);
        try {
            userService.add(duplicatePojo);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Name and Type combination already exists");
        }
    }

    @Test
    public void addAndGetByIdAndNameTypeTest() throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setName("c");
        userPojo.setType(UserType.CLIENT);
        userService.add(userPojo);
        UserPojo fetchedPojo = userService.getById(userPojo.getId());
        Assert.assertEquals(fetchedPojo.getId(), userPojo.getId());
        Assert.assertEquals(fetchedPojo.getName(), userPojo.getName());
        Assert.assertEquals(fetchedPojo.getType(), userPojo.getType());
        fetchedPojo = userService.getByNameAndType(userPojo.getName(), userPojo.getType());
        Assert.assertEquals(fetchedPojo.getId(), userPojo.getId());
        Assert.assertEquals(fetchedPojo.getName(), userPojo.getName());
        Assert.assertEquals(fetchedPojo.getType(), userPojo.getType());
    }

    @Test
    public void updateDuplicateCombinationTest() throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setName("c");
        userPojo.setType(UserType.CLIENT);
        userService.add(userPojo);
        try {
            userService.update(userPojo);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Name and Type combination already exists");
        }
    }

    @Test
    public void updateInvalidIdTest() {
        UserPojo userPojo = new UserPojo();
        userPojo.setId(1L);
        userPojo.setName("c");
        userPojo.setType(UserType.CLIENT);
        try {
            userService.update(userPojo);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "No user exists with given ID: 1");
        }
    }
}
