package com.increff.dto.helper;

import com.increff.constants.Const;
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
        if (channelIDMapForm == null) {
            rowErrors.add("Channel Form cannot be null");
        } else {
            if (BasicDataUtil.isEmpty(channelIDMapForm.getChannelSkuId())) {
                rowErrors.add("ChannelSkuId cannot be null or empty");
            } else if (channelIDMapForm.getChannelSkuId().length() > Const.MAX_LENGTH) {
                rowErrors.add("ChannelSkuId cannot exceed " + Const.MAX_LENGTH + " chars");
            }
            if (BasicDataUtil.isEmpty(channelIDMapForm.getClientSkuId())) {
                rowErrors.add("ClientSkuId cannot be null or empty");
            }
        }
        if (!rowErrors.isEmpty()) {
            errorMsg.append("row ").append(row).append(": ").append(rowErrors.get(0));
            for (int i = 1; i < rowErrors.size(); i++) {
                errorMsg.append(", ").append(rowErrors.get(i));
            }
            errorMsg.append(".\n");
        }
    }

    public static List<ChannelListingPojo> convertToPojoList(List<ChannelIDMapForm> channelIDMapFormList) {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for (ChannelIDMapForm channelIDMapForm : channelIDMapFormList) {
            ChannelListingPojo channelListingPojo = new ChannelListingPojo();
            channelListingPojo.setChannelSkuId(channelIDMapForm.getChannelSkuId());
            channelListingPojoList.add(channelListingPojo);
        }
        return channelListingPojoList;
    }

    public static void validateChannelForm(ChannelForm channelForm, StringBuilder errorMsg) {
        if (channelForm == null) {
            errorMsg.append("Channel Form cannot be null");
            return;
        }
        if (BasicDataUtil.isEmpty(channelForm.getName())) {
            errorMsg.append("Channel name cannot be null or empty");
        } else if (channelForm.getName().length() > Const.MAX_LENGTH) {
            errorMsg.append("Channel Name cannot exceed " + Const.MAX_LENGTH + " chars");
        }
        if (channelForm.getInvoiceType() == null) {
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

    private static ChannelListingData convertToChannelListingData(ChannelListingPojo channelListingPojo, String clientSkuId) {
        ChannelListingData channelListingData = new ChannelListingData();
        channelListingData.setChannelId(channelListingPojo.getChannelId());
        channelListingData.setChannelSkuId(channelListingPojo.getChannelSkuId());
        channelListingData.setClientSkuId(clientSkuId);
        channelListingData.setClientId(channelListingPojo.getClientId());
        return channelListingData;
    }

    public static List<ChannelListingData> convertToChannelListingDataList(List<ChannelListingPojo> channelListingPojoList, List<String> clientSkuIds) {
        List<ChannelListingData> channelListingDataList = new ArrayList<>();
        int i = 0;
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            channelListingDataList.add(convertToChannelListingData(channelListingPojo, clientSkuIds.get(i++)));
        }
        return channelListingDataList;
    }

    public static void normalize(ChannelForm channelForm) {
        channelForm.setName(BasicDataUtil.trimAndLowerCase(channelForm.getName()));
    }
}
