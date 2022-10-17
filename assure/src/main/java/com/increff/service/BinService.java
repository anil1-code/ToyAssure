package com.increff.service;

import com.increff.dao.BinDao;
import com.increff.exception.ApiException;
import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BinService {

    @Autowired
    private BinDao binDao;
    @Autowired
    private ProductService productService;

    @Transactional
    public BinPojo add(BinPojo binPojo) {
        return binDao.add(binPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<BinSkuPojo> addInventory(List<BinSkuPojo> binSkuPojoList) throws ApiException {
        List<BinSkuPojo> addedPojoList = new ArrayList<>();
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        for (BinSkuPojo binSkuPojo : binSkuPojoList) {
            List<String> rowErrors = new ArrayList<>();
            if (binDao.getBinPojoById(binSkuPojo.getBinId()) == null) {
                rowErrors.add("Bin doesn't exist");
            }
            if (productService.getByGlobalSkuId(binSkuPojo.getGlobalSkuId()) == null) {
                rowErrors.add("Product doesn't exist");
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
}
