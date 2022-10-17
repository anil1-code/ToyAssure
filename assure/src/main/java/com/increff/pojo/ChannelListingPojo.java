package com.increff.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_channel_listing", uniqueConstraints = {@UniqueConstraint(columnNames = {"channelId", "globalSkuId"})})
public class ChannelListingPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long id;
    @NotNull
    private Long channelId;
    @NotNull
    private String channelSkuId;
    @NotNull
    private Long clientId;
    @NotNull
    private Long globalSkuId;
}
