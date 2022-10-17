package com.increff.pojo;

import com.increff.util.ChannelType;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_channels", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class ChannelPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private ChannelType channelType;
}
