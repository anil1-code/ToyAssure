package com.increff.model.forms;

import com.increff.util.ChannelType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelForm {
    private String name;
    private ChannelType channelType;
}
