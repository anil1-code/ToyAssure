package com.increff.dto;

import com.increff.dto.helper.ChannelDtoHelper;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.service.ChannelService;
import exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        channelPojo.setInvoiceType(channelForm.getChannelType());
        return channelService.add(channelPojo);
    }

    public List<ChannelListingPojo> addChannelIDMappings(List<ChannelIDMapForm> channelIDMapFormList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        for (ChannelIDMapForm channelIDMapForm : channelIDMapFormList) {
            ChannelDtoHelper.validateAndNormalize(channelIDMapForm, row++, errorMsg);
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        return channelService.addChannelIDMappings(ChannelDtoHelper.convertToPojoList(channelIDMapFormList),
                ChannelDtoHelper.getChannelNameList(channelIDMapFormList), ChannelDtoHelper.getClientNameList(channelIDMapFormList));
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