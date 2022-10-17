package com.increff.dto;

import com.increff.dto.helper.UserDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.data.UserData;
import com.increff.model.forms.UserForm;
import com.increff.pojo.UserPojo;
import com.increff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserDto {
    @Autowired
    public UserService userService;

    public List<UserData> getAll() {
        List<UserPojo> userPojoList = userService.getAll();
        return UserDtoHelper.convertToDataList(userPojoList);
    }

    public UserPojo add(UserForm userForm) throws ApiException {
        UserDtoHelper.validate(userForm);
        UserDtoHelper.normalize(userForm);
        return userService.add(UserDtoHelper.convertFormToPojo(userForm));
    }

    public UserPojo update(Long id, UserForm userForm) throws ApiException {
        UserDtoHelper.validate(userForm);
        UserDtoHelper.normalize(userForm);
        UserPojo userPojo = UserDtoHelper.convertFormToPojo(userForm);
        userPojo.setId(id);
        return userService.update(userPojo);
    }

}
