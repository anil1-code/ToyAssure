package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.constants.Const;
import com.increff.model.data.UserData;
import com.increff.model.forms.UserForm;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserDtoTest extends AbstractUnitTest {
    @Autowired
    private UserDto userDto;

    @Test
    public void addTestNameNull() {
        UserForm userForm = new UserForm();
        userForm.setName(null);
        userForm.setType(UserType.CLIENT);
        try {
            userDto.add(userForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "User Name cannot be null or empty");
        }
    }

    @Test
    public void addTestNameEmpty() {
        UserForm userForm = new UserForm();
        userForm.setName("");
        userForm.setType(UserType.CUSTOMER);
        try {
            userDto.add(userForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "User Name cannot be null or empty");
        }
    }


    @Test
    public void addTestNullForm() {
        try {
            userDto.add(null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "User Form cannot be null");
        }
    }


    @Test
    public void addTestLongName() {
        UserForm userForm = new UserForm();
        userForm.setName("c".repeat(Const.MAX_LENGTH + 1));
        userForm.setType(UserType.CLIENT);
        try {
            userDto.add(userForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "User Name cannot exceed " + Const.MAX_LENGTH + " chars");
        }
    }

    @Test
    public void addAndGetTestValid() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName(" C ");
        userForm.setType(UserType.CLIENT);
        userDto.add(userForm);
        List<UserData> userDataList = userDto.getAll();
        Assert.assertNotEquals(userDataList, null);
        Assert.assertEquals(userDataList.size(), 1);
        Assert.assertEquals(userDataList.get(0).getName(), "c");
        Assert.assertEquals(userDataList.get(0).getUserType(), UserType.CLIENT);
    }

    @Test
    public void updateTestName() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName("c");
        userForm.setType(UserType.CLIENT);
        UserPojo userPojo = userDto.add(userForm);
        userForm.setName(" d ");
        userForm.setType(UserType.CUSTOMER);
        userDto.update(userPojo.getId(), userForm);
        List<UserData> userDataList = userDto.getAll();
        Assert.assertNotEquals(userDataList, null);
        Assert.assertEquals(userDataList.size(), 1);
        Assert.assertEquals(userDataList.get(0).getName(), "d");
        Assert.assertEquals(userDataList.get(0).getUserType(), UserType.CUSTOMER);
    }

}
