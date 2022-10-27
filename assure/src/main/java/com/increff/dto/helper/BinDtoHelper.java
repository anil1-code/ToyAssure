package com.increff.dto.helper;

import com.increff.model.data.BinData;
import com.increff.model.forms.BinwiseInventoryForm;
import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import com.increff.util.BasicDataUtil;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BinDtoHelper {
    public static void validate(BinwiseInventoryForm binwiseInventoryForm, int row, StringBuilder errorMsg) {
        List<String> errors = new ArrayList<>();
        if (binwiseInventoryForm == null) {
            errors.add("Form cannot be null");
        } else {
            if (binwiseInventoryForm.getBinId() == null) {
                errors.add("Bin Id cannot be null");
            }
            if (BasicDataUtil.isEmpty(binwiseInventoryForm.getClientSkuId())) {
                errors.add("ClientSkuId cannot be null or empty");
            }
            if (binwiseInventoryForm.getQuantity() == null) {
                errors.add("Quantity cannot be null");
            } else if (binwiseInventoryForm.getQuantity() < 1) {
                errors.add("Quantity cannot be less than 1");
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
