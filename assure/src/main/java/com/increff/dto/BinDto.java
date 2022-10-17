package com.increff.dto;

import com.increff.dto.helper.BinDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.forms.BinwiseInventoryForm;
import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import com.increff.service.BinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BinDto {
    @Autowired
    private BinService binService;

    public List<BinPojo> add(Long binCount) {
        List<BinPojo> addedPojoList = new ArrayList<>();
        for (int i = 0; i < binCount; i++) {
            BinPojo addedPojo = binService.add(new BinPojo());
            addedPojoList.add(addedPojo);
        }
        return addedPojoList;
    }

    public List<BinSkuPojo> addInventory(List<BinwiseInventoryForm> binwiseInventoryFormList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (BinwiseInventoryForm form : binwiseInventoryFormList) {
            BinDtoHelper.validate(form, row++, errorMsg);
            if (form != null)
                binSkuPojoList.add(BinDtoHelper.convertFormToPojo(form));
        }
        if (errorMsg.length() == 0) {
            return binService.addInventory(binSkuPojoList);
        }
        throw new ApiException(errorMsg.toString());
    }
}
