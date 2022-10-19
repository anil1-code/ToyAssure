package com.increff.dto.helper;

import com.increff.constants.Const;
import com.increff.model.data.UserData;
import com.increff.model.forms.UserForm;
import com.increff.pojo.UserPojo;
import com.increff.util.BasicDataUtil;
import exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class UserDtoHelper {
    public static List<UserData> convertToDataList(List<UserPojo> userPojoList) {
        List<UserData> userDataList = new ArrayList<>();
        for (UserPojo userPojo : userPojoList) {
            userDataList.add(convertToData(userPojo));
        }
        return userDataList;
    }

    public static UserData convertToData(UserPojo userPojo) {
        UserData userData = new UserData();
        userData.setId(userPojo.getId());
        userData.setUserType(userPojo.getType());
        userData.setName(userPojo.getName());
        return userData;
    }

    public static UserPojo convertFormToPojo(UserForm userForm) {
        UserPojo userPojo = new UserPojo();
        userPojo.setName(userForm.getName());
        userPojo.setType(userForm.getType());
        return userPojo;
    }

    public static void normalize(UserForm userForm) {
        userForm.setName(BasicDataUtil.trimAndLowerCase(userForm.getName()));
    }

    public static void validate(UserForm userForm) throws ApiException {
        if (BasicDataUtil.isEmpty(userForm.getName())) {
            throw new ApiException("Name cannot be empty");
        }
        if (userForm.getName().length() > Const.MAX_LENGTH) {
            throw new ApiException("Name cannot exceed " + Const.MAX_LENGTH + " chars");
        }
    }
}
