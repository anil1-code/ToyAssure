package com.increff.controller;

import com.increff.dto.UserDto;
import com.increff.exception.ApiException;
import com.increff.model.data.UserData;
import com.increff.model.forms.UserForm;
import com.increff.pojo.UserPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    @Autowired
    private UserDto userDto;

    @ApiOperation(value = "get all the users")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserData> getAll() {
        return userDto.getAll();
    }

    @ApiOperation(value = "add a users")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserPojo add(@RequestBody UserForm userForm) throws ApiException {
        return userDto.add(userForm);
    }

    @ApiOperation(value = "update a user")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public UserPojo update(@PathVariable Long id, @RequestBody UserForm userForm) throws ApiException {
        return userDto.update(id, userForm);
    }
}