package com.increff.dto.helper;

import com.increff.exception.ApiException;
import com.increff.model.forms.BinwiseInventoryForm;
import com.increff.pojo.BinSkuPojo;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BinDtoHelper {
    public static void validate(BinwiseInventoryForm binwiseInventoryForm, int row, StringBuilder errorMsg) throws ApiException {
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
            if (binwiseInventoryForm.getQuantity() == null) {
                errors.add("Quantity can't be empty");
            }
        }
        if (errors.size() != 0) {
            errorMsg.append("row ").append(row).append(": ").append(errors.get(0));
            for (int i = 1; i < errors.size(); i++) {
                errorMsg.append(", ");
                errorMsg.append(errors.get(i));
            }
            errorMsg.append(".\n");
            throw new ApiException(errorMsg.toString());
        }
    }

    public static BinSkuPojo convertFormToPojo(@NotNull BinwiseInventoryForm form) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setGlobalSkuId(form.getGlobalSkuId());
        binSkuPojo.setBinId(form.getBinId());
        binSkuPojo.setQuantity(form.getQuantity());
        return binSkuPojo;
    }
}
