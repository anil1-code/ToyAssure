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

    @Test(expected = ApiException.class)
    public void addTestNameNull() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName(null);
        userForm.setType(UserType.CLIENT);
        try {
            userDto.add(userForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Name cannot be empty");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addTestNameEmpty() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName("");
        userForm.setType(UserType.CUSTOMER);
        try {
            userDto.add(userForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Name cannot be empty");
            throw e;
        }
    }


    @Test(expected = ApiException.class)
    public void addTestLongName() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName("c".repeat(Const.MAX_LENGTH + 1));
        userForm.setType(UserType.CLIENT);
        try {
            userDto.add(userForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Name cannot exceed " + Const.MAX_LENGTH + " chars");
            throw e;
        }
    }

    @Test
    public void addAndGetTestValid() {
        UserForm userForm = new UserForm();
        userForm.setName(" C ");
        userForm.setType(UserType.CLIENT);
        try {
            userDto.add(userForm);
            List<UserData> userDataList = userDto.getAll();
            Assert.assertNotEquals(userDataList, null);
            Assert.assertEquals(userDataList.size(), 1);
            Assert.assertEquals(userDataList.get(0).getName(), "c");
            Assert.assertEquals(userDataList.get(0).getUserType(), UserType.CLIENT);
        } catch (ApiException e) {
            Assert.fail();
        }
    }

    @Test
    public void updateTestName() {
        UserForm userForm = new UserForm();
        userForm.setName("c");
        userForm.setType(UserType.CLIENT);
        try {
            UserPojo userPojo = userDto.add(userForm);
            userForm.setName(" d ");
            userForm.setType(UserType.CUSTOMER);
            userDto.update(userPojo.getId(), userForm);
            List<UserData> userDataList = userDto.getAll();
            Assert.assertNotEquals(userDataList, null);
            Assert.assertEquals(userDataList.size(), 1);
            Assert.assertEquals(userDataList.get(0).getName(), "d");
            Assert.assertEquals(userDataList.get(0).getUserType(), UserType.CUSTOMER);
        } catch (ApiException e) {
            Assert.fail();
        }
    }


}
