package com.increff.service;

import com.increff.dao.UserDao;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = true)
    public List<UserPojo> getAll() {
        return userDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public UserPojo add(UserPojo userPojo) throws ApiException {
        if (userDao.getByNameAndType(userPojo.getName(), userPojo.getType()) != null) {
            throw new ApiException("Name and Type combination already exists");
        }
        return userDao.add(userPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public UserPojo update(UserPojo userPojo) throws ApiException {
        if (userDao.getByNameAndType(userPojo.getName(), userPojo.getType()) != null) {
            throw new ApiException("Name and Type combination already exists");
        }
        UserPojo existingPojo = userDao.getById(userPojo.getId());
        if (existingPojo == null) {
            throw new ApiException("No user exists with given ID: " + userPojo.getId());
        }
        existingPojo.setName(userPojo.getName());
        existingPojo.setType(userPojo.getType());
        return existingPojo;
    }

    @Transactional(readOnly = true)
    public UserPojo getById(Long id) {
        return userDao.getById(id);
    }

    @Transactional(readOnly = true)
    public UserPojo getByNameAndType(String name, UserType type) {
        return userDao.getByNameAndType(name, type);
    }
}
