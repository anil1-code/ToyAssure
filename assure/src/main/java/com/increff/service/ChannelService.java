package com.increff.service;

import com.increff.dao.ChannelDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = ApiException.class)
    public ChannelPojo add(ChannelPojo channelPojo) throws ApiException {
        if (channelDao.getByName(channelPojo.getName()) != null) {
            throw new ApiException("Channel name already exists");
        }
        return channelDao.add(channelPojo);
    }


    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelListingPojo> addChannelIDMappings(List<ChannelListingPojo> channelListingPojoList, List<String> channelNames, List<String> clientNames) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        int i = 0;
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            List<String> rowErrors = new ArrayList<>();
            ChannelPojo channelPojo = channelDao.getByName(channelNames.get(i));
            UserPojo userPojo = userService.getByNameAndType(clientNames.get(i), UserType.CLIENT);
            if (channelPojo == null) {
                rowErrors.add("Channel does not exists");
            } else {
                channelListingPojo.setChannelId(channelPojo.getId());
            }
            if (userPojo == null) {
                rowErrors.add("Client does not exists");
            } else {
                channelListingPojo.setClientId(userPojo.getId());
            }
            if (!rowErrors.isEmpty()) {
                errorMsg.append("row ").append(i).append(": ").append(rowErrors.get(0));
                for (int j = 1; j < rowErrors.size(); j++) {
                    errorMsg.append(", ").append(rowErrors.get(j));
                }
                errorMsg.append(".\n");
            }
        }
        if (errorMsg.length() != 0)
            throw new ApiException(errorMsg.toString());
        List<ChannelListingPojo> addedPojoList = new ArrayList<>();
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            ChannelListingPojo existingPojo = channelDao.getByChannelAndGlobalSkuId(channelListingPojo.getChannelId(), channelListingPojo.getGlobalSkuId());
            if (existingPojo == null) {
                ChannelListingPojo addedPojo = channelDao.addChannelListingPojo(channelListingPojo);
                addedPojoList.add(addedPojo);
            } else {
                existingPojo.setChannelSkuId(channelListingPojo.getChannelSkuId());
                addedPojoList.add(existingPojo);
            }
        }
        return addedPojoList;
    }
}
