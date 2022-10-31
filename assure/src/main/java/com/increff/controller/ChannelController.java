package com.increff.controller;

import com.increff.dto.ChannelDto;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/channel")
public class ChannelController {
    @Autowired
    private ChannelDto channelDto;

    @ApiOperation("adds a channel")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ChannelPojo add(@RequestBody ChannelForm channelForm) throws ApiException {
        return channelDto.add(channelForm);
    }

    @ApiOperation("gets all the channels")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ChannelData> getAllChannels() {
        return channelDto.getAllChannels();
    }

    @ApiOperation("gets all the channel mappings")
    @RequestMapping(value = "/mappings", method = RequestMethod.GET)
    public List<ChannelListingData> getAllChannelMappings() {
        return channelDto.getAllChannelMappings();
    }

    @ApiOperation("adds the channel id mappings")
    @RequestMapping(value = "/mappings", method = RequestMethod.POST)
    public List<ChannelListingPojo> addChannelIDMappings(@RequestParam String clientName, @RequestParam String channelName, @RequestBody List<ChannelIDMapForm> channelIDMapFormList) throws ApiException {
        return channelDto.addChannelIDMappings(clientName, channelName, channelIDMapFormList);
    }

}
