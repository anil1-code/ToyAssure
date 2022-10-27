package com.increff.dto;

import com.increff.dto.helper.ChannelDtoHelper;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.service.ChannelService;
import com.increff.util.BasicDataUtil;
import exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChannelDto {
    @Autowired
    private ChannelService channelService;

    public ChannelPojo add(ChannelForm channelForm) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        ChannelDtoHelper.validateChannelForm(channelForm, errorMsg);
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        ChannelDtoHelper.normalize(channelForm);
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getInvoiceType());
        return channelService.add(channelPojo);
    }

    public List<ChannelListingPojo> addChannelIDMappings(String clientName, String channelName, List<ChannelIDMapForm> channelIDMapFormList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        if (BasicDataUtil.isEmpty(clientName)) {
            errorMsg.append("Client Name cannot be null or empty.\n");
        } else {
            clientName = BasicDataUtil.trimAndLowerCase(clientName);
        }
        if (BasicDataUtil.isEmpty(channelName)) {
            errorMsg.append("Channel Name cannot be null or empty.\n");
        } else {
            channelName = BasicDataUtil.trimAndLowerCase(channelName);
        }
        if (channelIDMapFormList == null) {
            errorMsg.append("ChannelSkuId list cannot be null.\n");
            throw new ApiException(errorMsg.toString());
        } else if (channelIDMapFormList.size() < 1) {
            errorMsg.append("ChannelSkuId list cannot be empty.\n");
            throw new ApiException(errorMsg.toString());
        } else {
            List<String> clientSkuIds = new ArrayList<>();
            int row = 1;
            for (ChannelIDMapForm channelIDMapForm : channelIDMapFormList) {
                ChannelDtoHelper.validateAndNormalize(channelIDMapForm, row++, errorMsg);
                if (channelIDMapForm != null) clientSkuIds.add(channelIDMapForm.getClientSkuId());
            }

            if (errorMsg.length() != 0) {
                throw new ApiException(errorMsg.toString());
            }
            return channelService.addChannelIDMappings(clientName, channelName,
                    ChannelDtoHelper.convertToPojoList(channelIDMapFormList),
                    clientSkuIds);
        }
    }

    public List<ChannelData> getAllChannels() {
        List<ChannelPojo> channelPojoList = channelService.getAllChannels();
        return ChannelDtoHelper.convertToChannelDataList(channelPojoList);
    }

    public List<ChannelListingData> getAllChannelMappings() {
        List<ChannelListingPojo> channelListingPojoList = channelService.getAllChannelMappings();
        return ChannelDtoHelper.convertToChannelListingDataList(channelListingPojoList);
    }
}