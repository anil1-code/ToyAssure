package com.increff.service;

import com.increff.config.AbstractUnitTest;
import com.increff.pojo.ChannelPojo;
import com.increff.util.InvoiceType;
import exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        ChannelPojo existingPojo = channelService.getByName(channelPojo.getName()); // removing this line breaks the code, find out why?
        ChannelPojo duplicatePojo = new ChannelPojo();
        duplicatePojo.setName("c");
        channelPojo.setInvoiceType(InvoiceType.SELF);
        try {
            channelService.add(duplicatePojo);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel name already exists");
        }
    }
}
