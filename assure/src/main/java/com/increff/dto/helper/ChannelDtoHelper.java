package com.increff.dto.helper;

import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.util.BasicDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ChannelDtoHelper {
    public static void validateAndNormalize(ChannelIDMapForm channelIDMapForm, int row, StringBuilder errorMsg) {
        List<String> rowErrors = new ArrayList<>();
        if (BasicDataUtil.isEmpty(channelIDMapForm.getChannelName())) {
            rowErrors.add("Channel Name can't be empty");
        } else {
            channelIDMapForm.setChannelName(BasicDataUtil.trimAndLowerCase(channelIDMapForm.getChannelName()));
        }
        if (BasicDataUtil.isEmpty(channelIDMapForm.getClientName())) {
            rowErrors.add("Client Name can't be empty");
        } else {
            channelIDMapForm.setClientName(BasicDataUtil.trimAndLowerCase(channelIDMapForm.getClientName()));
        }
        if (channelIDMapForm.getChannelSkuId() == null) {
            rowErrors.add("ChannelSkuId can't be null");
        }
        if (channelIDMapForm.getGlobalSkuId() == null) {
            rowErrors.add("GlobalSkuId can't be null");
        }
        if (!rowErrors.isEmpty()) {
            errorMsg.append("row ").append(row).append(": ").append(rowErrors.get(0));
            for (int i = 1; i < rowErrors.size(); i++) {
                errorMsg.append(", ").append(rowErrors.get(i));
            }
            errorMsg.append("\n");
        }
    }

    public static List<ChannelListingPojo> convertToPojoList(List<ChannelIDMapForm> channelIDMapFormList) {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for (ChannelIDMapForm channelIDMapForm : channelIDMapFormList) {
            ChannelListingPojo channelListingPojo = new ChannelListingPojo();
            channelListingPojo.setChannelSkuId(channelIDMapForm.getChannelSkuId());
            channelListingPojo.setGlobalSkuId(channelIDMapForm.getGlobalSkuId());
            channelListingPojoList.add(channelListingPojo);
        }
        return channelListingPojoList;
    }

    public static List<String> getChannelNameList(List<ChannelIDMapForm> channelIDMapFormList) {
        List<String> channelNames = new ArrayList<>();
        for (ChannelIDMapForm channelIDMapForm : channelIDMapFormList) {
            channelNames.add(channelIDMapForm.getChannelName());
        }
        return channelNames;
    }

    public static List<String> getClientNameList(List<ChannelIDMapForm> channelIDMapFormList) {
        List<String> clientNames = new ArrayList<>();
        for (ChannelIDMapForm channelIDMapForm : channelIDMapFormList) {
            clientNames.add(channelIDMapForm.getClientName());
        }
        return clientNames;
    }

    public static void validateChannelForm(ChannelForm channelForm, StringBuilder errorMsg) {
        if (BasicDataUtil.isEmpty(channelForm.getName())) {
            errorMsg.append("Channel name cannot be empty");
        }
        if (channelForm.getChannelType() == null) {
            errorMsg.append(errorMsg.length() == 0 ? "" : ", ").append("Channel Type cannot be null");
        }
    }

    public static List<ChannelData> convertToChannelDataList(List<ChannelPojo> channelPojoList) {
        List<ChannelData> channelDataList = new ArrayList<>();
        for (ChannelPojo channelPojo : channelPojoList) {
            channelDataList.add(convertToChannelData(channelPojo));
        }
        return channelDataList;
    }

    private static ChannelData convertToChannelData(ChannelPojo channelPojo) {
        ChannelData channelData = new ChannelData();
        channelData.setId(channelPojo.getId());
        channelData.setName(channelPojo.getName());
        channelData.setInvoiceType(channelPojo.getInvoiceType());
        return channelData;
    }

    private static ChannelListingData convertToChannelListingData(ChannelListingPojo channelListingPojo) {
        ChannelListingData channelListingData = new ChannelListingData();
        channelListingData.setChannelId(channelListingPojo.getChannelId());
        channelListingData.setChannelSkuId(channelListingPojo.getChannelSkuId());
        channelListingData.setGlobalSkuId(channelListingPojo.getGlobalSkuId());
        channelListingData.setClientId(channelListingPojo.getClientId());
        return channelListingData;
    }

    public static List<ChannelListingData> convertToChannelListingDataList(List<ChannelListingPojo> channelListingPojoList) {
        List<ChannelListingData> channelListingDataList = new ArrayList<>();
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            channelListingDataList.add(convertToChannelListingData(channelListingPojo));
        }
        return channelListingDataList;
    }

    public static void normalize(ChannelForm channelForm) {
        channelForm.setName(BasicDataUtil.trimAndLowerCase(channelForm.getName()));
    }
}
