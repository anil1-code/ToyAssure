package com.increff.service;

import com.increff.dao.ChannelDao;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.ProductPojo;
import com.increff.pojo.UserPojo;
import com.increff.util.UserType;
import exception.ApiException;
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
    @Autowired
    private ProductService productService;

    @Transactional(rollbackFor = ApiException.class)
    public ChannelPojo add(ChannelPojo channelPojo) throws ApiException {
        ChannelPojo existingPojo = getByName(channelPojo.getName());
        if (existingPojo != null) {
            throw new ApiException("Channel name already exists");
        }
        return channelDao.add(channelPojo);
    }


    @Transactional(rollbackFor = ApiException.class)
    public List<ChannelListingPojo> addChannelIDMappings(String clientName, String channelName, List<ChannelListingPojo> channelListingPojoList, List<String> clientSkuIds) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        ChannelPojo channelPojo = getByName(channelName);
        UserPojo userPojo = userService.getByNameAndType(clientName, UserType.CLIENT);
        if (channelPojo == null) {
            errorMsg.append("Channel does not exists.\n");
        }
        if (userPojo == null || userPojo.getType() != UserType.CLIENT) {
            errorMsg.append("Client does not exists.\n");
        }
        int i = 0;
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            if (channelPojo != null)
                channelListingPojo.setChannelId(channelPojo.getId());
            if (userPojo != null && userPojo.getType() == UserType.CLIENT)
                channelListingPojo.setClientId(userPojo.getId());
            ProductPojo productPojo = userPojo == null ? null : productService.getByClientAndClientSkuId(userPojo.getId(), clientSkuIds.get(i));
            if (productPojo == null) {
                errorMsg.append("row ").append(i + 1).append(": ").append("Product does not exists.\n");
            } else {
                channelListingPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
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

    @Transactional
    public ChannelPojo getByName(String channelName) {
        return channelDao.getByName(channelName);
    }

    @Transactional(readOnly = true)
    public ChannelPojo getById(Long channelId) {
        return channelDao.getById(channelId);
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo getByClientChannelAndChannelSkuId(Long clientId, Long channelId, String channelSkuId) {
        return channelDao.getByClientChannelAndChannelSkuId(clientId, channelId, channelSkuId);
    }

    @Transactional(readOnly = true)
    public List<ChannelPojo> getAllChannels() {
        return channelDao.getAllChannels();
    }

    @Transactional(readOnly = true)
    public List<ChannelListingPojo> getAllChannelMappings(List<String> clientSkuIds) {
        List<ChannelListingPojo> channelListingPojoList = channelDao.getAllChannelMappings();
        for (ChannelListingPojo channelListingPojo : channelListingPojoList) {
            ProductPojo productPojo = productService.getByGlobalSkuId(channelListingPojo.getGlobalSkuId());
            clientSkuIds.add(productPojo.getClientSkuId());
        }
        return channelListingPojoList;
    }
}
