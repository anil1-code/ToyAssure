package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.constants.Const;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.model.forms.ProductForm;
import com.increff.model.forms.UserForm;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.ProductPojo;
import com.increff.pojo.UserPojo;
import com.increff.util.InvoiceType;
import com.increff.util.UserType;
import exception.ApiException;
import org.h2.engine.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class ChannelDtoTest extends AbstractUnitTest {
    @Autowired
    private ChannelDto channelDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private UserDto userDto;

    @Test(expected = ApiException.class)
    public void addTestNullInputs() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        try {
            channelDto.add(channelForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel name cannot be empty, Channel Type cannot be null");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addTestEmptyInputs() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName("");
        try {
            channelDto.add(channelForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel name cannot be empty, Channel Type cannot be null");
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void addTestMaxInputs() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName("C".repeat(31));
        try {
            channelDto.add(channelForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Name cannot exceed " + Const.MAX_LENGTH + " chars, Channel Type cannot be null");
            throw e;
        }
    }

    @Test
    public void addAndGetTestValidInputs() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName(" C ");
        channelForm.setInvoiceType(InvoiceType.CHANNEL);
        try {
            ChannelPojo channelPojo = channelDto.add(channelForm);
            Assert.assertNotEquals(channelPojo, null);
            Assert.assertEquals(channelPojo.getName(), "c");
            Assert.assertEquals(channelPojo.getInvoiceType(), InvoiceType.CHANNEL);
        } catch (ApiException e) {
            Assert.fail();
        }
        List<ChannelData> channelDataList = channelDto.getAllChannels();
        Assert.assertNotEquals(channelDataList, null);
        Assert.assertEquals(channelDataList.size(), 1);
        Assert.assertEquals(channelDataList.get(0).getName(), "c");
        Assert.assertEquals(channelDataList.get(0).getInvoiceType(), InvoiceType.CHANNEL);
    }

    @Test(expected = ApiException.class)
    public void addChannelIDMappingsTestEmptyInputs() throws ApiException {
        List<ChannelIDMapForm> channelIDMapFormList = new ArrayList<>();
        ChannelIDMapForm channelIDMapForm = new ChannelIDMapForm();
        channelIDMapForm.setChannelName("");
        channelIDMapForm.setChannelSkuId("");
        channelIDMapForm.setClientName("");
        channelIDMapForm.setGlobalSkuId(null);
        channelIDMapFormList.add(channelIDMapForm);
        channelIDMapFormList.add(null);
        try {
            channelDto.addChannelIDMappings(channelIDMapFormList);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Channel Name can't be empty, Client Name can't be empty, ChannelSkuId can't be empty, GlobalSkuId can't be null.\nrow 2: Channel Form can't be null.\n");
            throw e;
        }
    }

    @Test
    public void addAndGetChannelIDMappingsTestValidInputs() {
        ChannelPojo channelPojo = null;
        ProductPojo productPojo = null;
        UserPojo userPojo = null;
        try {
            userPojo = createUser();
            productPojo = createProduct(userPojo);
            channelPojo = createChannel();
            ChannelIDMapForm channelIDMapForm = new ChannelIDMapForm();
            channelIDMapForm.setChannelSkuId("channel sku id");
            channelIDMapForm.setChannelName(channelPojo.getName());
            channelIDMapForm.setClientName(userPojo.getName());
            channelIDMapForm.setGlobalSkuId(productPojo.getGlobalSkuId());
            channelDto.addChannelIDMappings(List.of(channelIDMapForm));
        } catch (ApiException e) {
            Assert.fail();
        }
        List<ChannelListingData> channelListingDataList = channelDto.getAllChannelMappings();
        Assert.assertNotEquals(channelListingDataList, null);
        Assert.assertEquals(channelListingDataList.size(), 1);
        Assert.assertEquals(channelListingDataList.get(0).getChannelId(), channelPojo.getId());
        Assert.assertEquals(channelListingDataList.get(0).getClientId(), productPojo.getClientId());
        Assert.assertEquals(channelListingDataList.get(0).getGlobalSkuId(), productPojo.getGlobalSkuId());
        Assert.assertEquals(channelListingDataList.get(0).getChannelSkuId(), "channel sku id");
    }


    private UserPojo createUser() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setName("temp user");
        userForm.setType(UserType.CLIENT);
        return userDto.add(userForm);
    }

    private ProductPojo createProduct(UserPojo userPojo) throws ApiException {
        ProductForm productForm = new ProductForm();
        productForm.setMrp(100d);
        productForm.setDescription("desc");
        productForm.setName("p name");
        productForm.setBrandId("bid");
        productForm.setClientSkuId("c sku id");
        productForm.setClientId(userPojo.getId());
        return productDto.add(List.of(productForm)).get(0);
    }

    private ChannelPojo createChannel() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName("C");
        channelForm.setInvoiceType(InvoiceType.CHANNEL);
        return channelDto.add(channelForm);
    }
}
