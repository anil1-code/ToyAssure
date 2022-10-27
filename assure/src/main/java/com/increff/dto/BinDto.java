package com.increff.dto;

import com.increff.dto.helper.BinDtoHelper;
import com.increff.model.data.BinData;
import com.increff.model.forms.BinwiseInventoryForm;
import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import com.increff.service.BinService;
import exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BinDto {
    @Autowired
    private BinService binService;

    public List<BinPojo> add(Long binCount) throws ApiException {
        if (binCount < 1) {
            throw new ApiException("Bin count cannot be less than 1");
        }
        return binService.add(binCount);
    }

    public List<BinSkuPojo> addInventory(Long clientId, List<BinwiseInventoryForm> binwiseInventoryFormList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        if (clientId == null) {
            errorMsg.append("Client ID cannot be null.\n");
        }
        int row = 1;
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        List<String> clientSkuIds = new ArrayList<>();
        for (BinwiseInventoryForm form : binwiseInventoryFormList) {
            BinDtoHelper.validate(form, row++, errorMsg);
            if (form != null) {
                binSkuPojoList.add(BinDtoHelper.convertFormToPojo(form));
                clientSkuIds.add(form.getClientSkuId());
            }
        }
        if (errorMsg.length() == 0) {
            return binService.addInventory(clientId, binSkuPojoList, clientSkuIds);
        }
        throw new ApiException(errorMsg.toString());
    }

    public List<BinData> getAll() {
        List<BinPojo> binPojoList = binService.getAll();
        return BinDtoHelper.convertToDataList(binPojoList);
    }
}
