package com.increff.service;

import com.increff.dao.ProductDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ProductPojo;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserService userService;


    @Transactional(readOnly = true)
    public List<ProductPojo> getAll() {
        return productDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<ProductPojo> add(List<ProductPojo> productPojoList) throws ApiException {
        List<ProductPojo> addedList = new ArrayList<>();
        int row = 1;
        StringBuilder errorMsg = new StringBuilder();
        for (ProductPojo productPojo : productPojoList) {
            UserPojo userPojo = userService.getById(productPojo.getClientId());
            if (userPojo == null || userPojo.getType() == UserType.CUSTOMER) {
                // not a valid client id
                errorMsg.append("row ").append(row).append(": ").append("Invalid Client Id\n");
            } else if (productDao.getByClientAndClientSku(productPojo.getClientId(), productPojo.getClientSkuId()) != null) {
                // clientId and clientSkuId combination already exists
                errorMsg.append("row ").append(row).append(": ").append("ClientId and ClientSkuId combination already exists\n");
            } else {
                // add
                ProductPojo addedPojo = productDao.add(productPojo);
                addedList.add(addedPojo);
            }
            row++;
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        return addedList;
    }


    @Transactional(rollbackFor = ApiException.class)
    public ProductPojo update(ProductPojo productPojo) throws ApiException {
        ProductPojo existingPojo = productDao.getByGlobalSkuId(productPojo.getGlobalSkuId());
        if (existingPojo == null) {
            throw new ApiException("Product doesn't exists");
        }
        existingPojo.setName(productPojo.getName());
        existingPojo.setBrandId(productPojo.getBrandId());
        existingPojo.setMrp(productPojo.getMrp());
        existingPojo.setDescription(productPojo.getDescription());
        return existingPojo;
    }

    @Transactional
    public ProductPojo getByGlobalSkuId(Long globalSkuId) {
        return productDao.getByGlobalSkuId(globalSkuId);
    }
}
