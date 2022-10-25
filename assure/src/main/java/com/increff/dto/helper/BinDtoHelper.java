package com.increff.dto.helper;

import com.increff.model.data.BinData;
import com.increff.model.forms.BinwiseInventoryForm;
import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import com.sun.istack.NotNull;
import exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class BinDtoHelper {
    public static void validate(BinwiseInventoryForm binwiseInventoryForm, int row, StringBuilder errorMsg) {
        List<String> errors = new ArrayList<>();
        if (binwiseInventoryForm == null) {
            errors.add("Form can't be null");
        } else {
            if (binwiseInventoryForm.getBinId() == null) {
                errors.add("Bin Id can't be empty");
            }
            if (binwiseInventoryForm.getGlobalSkuId() == null) {
                errors.add("GlobalSkuId can't be empty");
            }
            if (binwiseInventoryForm.getQuantity() == null || binwiseInventoryForm.getQuantity() < 1) {
                errors.add("Quantity can't be empty or less than 1");
            }
        }
        if (errors.size() != 0) {
            errorMsg.append("row ").append(row).append(": ").append(errors.get(0));
            for (int i = 1; i < errors.size(); i++) {
                errorMsg.append(", ");
                errorMsg.append(errors.get(i));
            }
            errorMsg.append(".\n");
        }
    }

    public static BinSkuPojo convertFormToPojo(@NotNull BinwiseInventoryForm form) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setGlobalSkuId(form.getGlobalSkuId());
        binSkuPojo.setBinId(form.getBinId());
        binSkuPojo.setQuantity(form.getQuantity());
        return binSkuPojo;
    }

    public static List<BinData> convertToDataList(List<BinPojo> binPojoList) {
        List<BinData> binDataList = new ArrayList<>();
        for (BinPojo binPojo : binPojoList) {
            binDataList.add(convertToData(binPojo));
        }
        return binDataList;
    }

    private static BinData convertToData(BinPojo binPojo) {
        BinData binData = new BinData();
        binData.setId(binPojo.getId());
        return binData;
    }
}
