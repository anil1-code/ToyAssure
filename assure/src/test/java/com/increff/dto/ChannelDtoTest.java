package com.increff.dto;

import com.increff.config.AbstractUnitTest;
import com.increff.constants.Const;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.model.forms.ProductForm;
import com.increff.model.forms.UserForm;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.ProductPojo;
import com.increff.pojo.UserPojo;
import com.increff.service.ChannelService;
import com.increff.util.InvoiceType;
import com.increff.util.UserType;
import exception.ApiException;
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
    @Autowired
    private ChannelService channelService;

    @Test
    public void addTestNullForm() {
        try {
            channelDto.add(null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel Form cannot be null");
        }
    }

    @Test
    public void addTestNullInputs() {
        ChannelForm channelForm = new ChannelForm();
        try {
            channelDto.add(channelForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel name cannot be null or empty, Channel Type cannot be null");
        }
    }

    @Test
    public void addTestEmptyInputs() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName("");
        try {
            channelDto.add(channelForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel name cannot be null or empty, Channel Type cannot be null");
        }
    }

    @Test
    public void addTestMaxInputs() {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName("C".repeat(Const.MAX_LENGTH + 1));
        try {
            channelDto.add(channelForm);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Channel Name cannot exceed " + Const.MAX_LENGTH + " chars, Channel Type cannot be null");
        }
    }

    @Test
    public void addAndGetTestValidInputs() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName(" C ");
        channelForm.setInvoiceType(InvoiceType.CHANNEL);
        ChannelPojo channelPojo = channelDto.add(channelForm);
        Assert.assertNotEquals(channelPojo, null);
        Assert.assertEquals(channelPojo.getName(), "c");
        Assert.assertEquals(channelPojo.getInvoiceType(), InvoiceType.CHANNEL);
        List<ChannelData> channelDataList = channelDto.getAllChannels();
        Assert.assertNotEquals(channelDataList, null);
        Assert.assertEquals(channelDataList.size(), 1);
        Assert.assertEquals(channelDataList.get(0).getName(), "c");
        Assert.assertEquals(channelDataList.get(0).getInvoiceType(), InvoiceType.CHANNEL);
    }

    @Test
    public void addChannelMappingsNullInputs() {
        try {
            channelDto.addChannelIDMappings(null, null, null);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Client Name cannot be null or empty.\nChannel Name cannot be null or empty.\nChannelSkuId list cannot be null.\n");
        }
    }

    @Test
    public void addChannelMappingsEmptyInputs() {
        try {
            channelDto.addChannelIDMappings("", "", new ArrayList<>());
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "Client Name cannot be null or empty.\nChannel Name cannot be null or empty.\nChannelSkuId list cannot be empty.\n");
        }
    }

    @Test
    public void addChannelMappingsInvalidInputs() {
        List<ChannelIDMapForm> channelIDMapFormList = new ArrayList<>();
        channelIDMapFormList.add(null);
        ChannelIDMapForm nullInputForm = new ChannelIDMapForm();
        nullInputForm.setChannelSkuId(null);
        nullInputForm.setClientSkuId(null);
        channelIDMapFormList.add(nullInputForm);
        ChannelIDMapForm emptyInputForm = new ChannelIDMapForm();
        emptyInputForm.setChannelSkuId("");
        emptyInputForm.setClientSkuId("");
        channelIDMapFormList.add(emptyInputForm);
        ChannelIDMapForm largeInputForm = new ChannelIDMapForm();
        largeInputForm.setChannelSkuId("c".repeat(Const.MAX_LENGTH + 1));
        largeInputForm.setClientSkuId("s");
        channelIDMapFormList.add(largeInputForm);
        try {
            channelDto.addChannelIDMappings("x", "y", channelIDMapFormList);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "row 1: Channel Form cannot be null.\nrow 2: ChannelSkuId cannot be null or empty, ClientSkuId cannot be null or empty.\nrow 3: ChannelSkuId cannot be null or empty, ClientSkuId cannot be null or empty.\nrow 4: ChannelSkuId cannot exceed " + Const.MAX_LENGTH + " chars.\n");
        }
    }

    @Test
    public void addAndGetChannelIDMappingsValid() throws ApiException {
        UserPojo userPojo = createUser();
        ProductPojo productPojo = createProduct(userPojo);
        ChannelPojo channelPojo = createChannel();

        ChannelIDMapForm channelIDMapForm = new ChannelIDMapForm();
        channelIDMapForm.setChannelSkuId("sku");
        channelIDMapForm.setClientSkuId(productPojo.getClientSkuId());
        channelDto.addChannelIDMappings(userPojo.getName(), channelPojo.getName(), List.of(channelIDMapForm));

        List<ChannelListingData> channelListingDataList = channelDto.getAllChannelMappings();
        Assert.assertNotEquals(channelListingDataList, null);
        Assert.assertEquals(channelListingDataList.size(), 1);
        Assert.assertEquals(channelListingDataList.get(0).getChannelId(), channelPojo.getId());
        Assert.assertEquals(channelListingDataList.get(0).getChannelSkuId(), "sku");
        Assert.assertEquals(channelListingDataList.get(0).getClientId(), userPojo.getId());
        Assert.assertEquals(channelListingDataList.get(0).getGlobalSkuId(), productPojo.getGlobalSkuId());


        ChannelIDMapForm updateIdForm = new ChannelIDMapForm();
        updateIdForm.setChannelSkuId("updated_sku");
        updateIdForm.setClientSkuId(productPojo.getClientSkuId());
        channelDto.addChannelIDMappings(userPojo.getName(), channelPojo.getName(), List.of(updateIdForm));

        channelListingDataList = channelDto.getAllChannelMappings();
        Assert.assertNotEquals(channelListingDataList, null);
        Assert.assertEquals(channelListingDataList.size(), 1);
        Assert.assertEquals(channelListingDataList.get(0).getChannelId(), channelPojo.getId());
        Assert.assertEquals(channelListingDataList.get(0).getChannelSkuId(), "updated_sku");
        Assert.assertEquals(channelListingDataList.get(0).getClientId(), userPojo.getId());
        Assert.assertEquals(channelListingDataList.get(0).getGlobalSkuId(), productPojo.getGlobalSkuId());

        ChannelPojo retrievedChannel = channelService.getById(channelPojo.getId());
        Assert.assertNotEquals(retrievedChannel, null);
        Assert.assertEquals(retrievedChannel.getName(), "c");
        Assert.assertEquals(retrievedChannel.getInvoiceType(), InvoiceType.CHANNEL);
        ChannelListingPojo retrievedChannelListing = channelService.getByClientChannelAndChannelSkuId(userPojo.getId(), channelPojo.getId(), "updated_sku");
        Assert.assertNotEquals(retrievedChannelListing, null);
        Assert.assertEquals(retrievedChannelListing.getChannelId(), channelPojo.getId());
        Assert.assertEquals(retrievedChannelListing.getChannelSkuId(), "updated_sku");
        Assert.assertEquals(retrievedChannelListing.getClientId(), userPojo.getId());
        Assert.assertEquals(retrievedChannelListing.getGlobalSkuId(), productPojo.getGlobalSkuId());
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
