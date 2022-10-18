package com.increff.dto;

import com.increff.dto.helper.ChannelDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.service.ChannelService;
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
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getChannelType());
        return channelService.add(channelPojo);
    }

    public List<ChannelListingPojo> addChannelIDMappings(List<ChannelIDMapForm> channelIDMapFormList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        int row = 1;
        for (ChannelIDMapForm channelIDMapForm : channelIDMapFormList) {
            ChannelDtoHelper.validate(channelIDMapForm, row++, errorMsg);
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        return channelService.addChannelIDMappings(ChannelDtoHelper.convertToPojoList(channelIDMapFormList),
                ChannelDtoHelper.getChannelNameList(channelIDMapFormList), ChannelDtoHelper.getClientNameList(channelIDMapFormList));
    }
}