package com.increff.controller;

import com.increff.dto.ChannelDto;
import com.increff.exception.ApiException;
import com.increff.model.forms.ChannelForm;
import com.increff.model.forms.ChannelIDMapForm;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("adds the channel id mappings")
    @RequestMapping(value = "/mappings", method = RequestMethod.POST)
    public List<ChannelListingPojo> addChannelIDMappings(@RequestBody List<ChannelIDMapForm> channelIDMapFormList) throws ApiException {
        return channelDto.addChannelIDMappings(channelIDMapFormList);
    }


}
