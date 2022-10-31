package com.increff.service;

import com.increff.config.AbstractUnitTest;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.util.InvoiceType;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class ChannelServiceTest extends AbstractUnitTest {
    @Autowired
    private ChannelService channelService;

    @Test
    public void addDuplicateNameTest() throws ApiException {
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName("c");
        channelPojo.setInvoiceType(InvoiceType.CHANNEL);
        channelService.add(channelPojo);
        ChannelPojo duplicatePojo = new ChannelPojo();
        duplicatePojo.setName("c");
        duplicatePojo.setInvoiceType(InvoiceType.SELF);
        try {
            channelService.add(duplicatePojo);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel name already exists");
        }
    }

    @Test
    public void addChannelIdMappingsInvalidTest() {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();
        channelListingPojo.setChannelSkuId("ch_sku");
        try {
            channelService.addChannelIDMappings("x", "y", List.of(channelListingPojo), List.of("c_sku"));
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel does not exists.\nClient does not exists.\nrow 1: Product does not exists.\n");
        }
    }


}
