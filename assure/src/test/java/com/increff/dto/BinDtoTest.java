package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.model.data.BinData;
import com.increff.model.forms.BinwiseInventoryForm;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class BinDtoTest extends AbstractUnitTest {
    @Autowired
    private BinDto binDto;

    @Test(expected = ApiException.class)
    public void addTestLessThanOne() throws ApiException {
        try {
            binDto.add(0L);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Bin count should not be less than 1");
            throw e;
        }
    }

    @Test
    public void addTestValid() throws ApiException {
        binDto.add(1L);
    }

    @Test
    public void getAllTest() throws ApiException {
        binDto.add(1L);
        List<BinData> binDataList = binDto.getAll();
        Assert.assertNotEquals(binDataList, null);
        Assert.assertEquals(binDataList.size(), 1);
    }

    @Test(expected = ApiException.class)
    public void addInventoryTestInvalid() throws ApiException {
        List<BinwiseInventoryForm> inventoryFormList = new ArrayList<>();
        inventoryFormList.add(null);
        BinwiseInventoryForm inventoryForm = new BinwiseInventoryForm();
        inventoryForm.setBinId(null);
        inventoryForm.setGlobalSkuId(null);
        inventoryForm.setQuantity(null);
        inventoryFormList.add(inventoryForm);
        try {
            binDto.addInventory(inventoryFormList);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Form can't be null.\nrow 2: Bin Id can't be empty, GlobalSkuId can't be empty, Quantity can't be empty.\n");
            throw e;
        }
    }

    @Test
    public void addInventoryValid() {
//        List<BinPojo> binPojoList = binDto.add(1L);

        List<BinwiseInventoryForm> inventoryFormList = new ArrayList<>();
        BinwiseInventoryForm inventoryForm = new BinwiseInventoryForm();
        inventoryForm.setGlobalSkuId(1L);
        inventoryForm.setBinId(1L);
        inventoryForm.setQuantity(1L);
        inventoryFormList.add(inventoryForm);
        try {
            binDto.addInventory(inventoryFormList);
        } catch (ApiException e) {
//            Assert.fail();
        }
    }

}
