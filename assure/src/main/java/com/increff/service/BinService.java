package com.increff.service;

import com.increff.dao.BinDao;
import com.increff.pojo.*;
import com.increff.util.UserType;
import exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.OrderItemPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BinService {

    @Autowired
    private BinDao binDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Transactional
    public List<BinPojo> add(Long binCount) {
        List<BinPojo> addedPojoList = new ArrayList<>();
        for (int i = 0; i < binCount; i++) {
            BinPojo addedPojo = binDao.add(new BinPojo());
            addedPojoList.add(addedPojo);
        }
        return addedPojoList;
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<BinSkuPojo> addInventory(Long clientId, List<BinSkuPojo> binSkuPojoList, List<String> clientSkuIds) throws ApiException {
        UserPojo userPojo = userService.getById(clientId);
        if (userPojo == null || userPojo.getType() != UserType.CLIENT) {
            throw new ApiException("No Client exists with given ID: " + clientId);
        }
        List<BinSkuPojo> addedPojoList = new ArrayList<>();
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            List<String> rowErrors = new ArrayList<>();
            if (binDao.getBinPojoById(binSkuPojo.getBinId()) == null) {
                rowErrors.add("Bin doesn't exist");
            }
            ProductPojo productPojo = productService.getByClientAndClientSkuId(clientId, clientSkuIds.get(row - 1));
            if (productPojo == null) {
                rowErrors.add("Product doesn't exist");
            } else {
                binSkuPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
            }
            BinSkuPojo existingPojo = binDao.getBinSkuInsideBin(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            Long addedQuantity = 0L;
            if (existingPojo == null) {
                BinSkuPojo addedPojo = binDao.addBinSkuPojo(binSkuPojo);
                addedPojoList.add(addedPojo);
                addedQuantity += binSkuPojo.getQuantity();
            } else {
                addedQuantity += binSkuPojo.getQuantity() - existingPojo.getQuantity();
                existingPojo.setQuantity(binSkuPojo.getQuantity());
                addedPojoList.add(existingPojo);
            }
            InventoryPojo existingInventoryPojo = binDao.getInventoryPojoByGlobalSkuId(binSkuPojo.getGlobalSkuId());
            if (existingInventoryPojo == null) {
                InventoryPojo inventoryPojo = new InventoryPojo();
                inventoryPojo.setAllocatedQuantity(0L);
                inventoryPojo.setFulfilledQuantity(0L);
                inventoryPojo.setAvailableQuantity(addedQuantity);
                inventoryPojo.setGlobalSkuId(binSkuPojo.getGlobalSkuId());
                binDao.addInventoryPojo(inventoryPojo);
            } else {
                existingInventoryPojo.setAvailableQuantity(existingInventoryPojo.getAvailableQuantity() + addedQuantity);
            }
            if (!rowErrors.isEmpty()) {
                errorMsg.append("row ").append(row).append(": ").append(rowErrors.get(0));
                for (int i = 1; i < rowErrors.size(); i++) {
                    errorMsg.append(", ");
                    errorMsg.append(rowErrors.get(i));
                }
                errorMsg.append(".\n");
            }
            row++;
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        return addedPojoList;
    }

    @Transactional
    public Long allocateBinSkus(OrderItemPojo orderItemPojo) { // returns the quantity allocated by this method call
        List<BinSkuPojo> binSkuPojoList = binDao.getBinSkusByGlobalSkuIds(orderItemPojo.getGlobalSkuId());
        Long totalAllocated = 0L; // total allocated by this method call only
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            Long allocate = Math.min(orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity(), binSkuPojo.getQuantity());
            binSkuPojo.setQuantity(binSkuPojo.getQuantity() - allocate);
            orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity() + allocate);
            totalAllocated += allocate;
            if (Objects.equals(orderItemPojo.getOrderedQuantity(), orderItemPojo.getAllocatedQuantity())) break;
        }
        return totalAllocated;
    }

    @Transactional
    public void allocateInventory(Long globalSkuId, Long allocated) {
        InventoryPojo inventoryPojo = binDao.getInventoryPojoByGlobalSkuId(globalSkuId);
        inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() - allocated);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() + allocated);
    }

    @Transactional
    public void fulfillInventory(Long globalSkuId, Long fulfill) {
        InventoryPojo inventoryPojo = binDao.getInventoryPojoByGlobalSkuId(globalSkuId);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() - fulfill);
        inventoryPojo.setFulfilledQuantity(inventoryPojo.getFulfilledQuantity() + fulfill);
    }

    @Transactional
    public List<BinPojo> getAll() {
        return binDao.getAll();
    }
}
