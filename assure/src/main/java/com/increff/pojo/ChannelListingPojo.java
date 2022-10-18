package com.increff.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_channel_listing", uniqueConstraints = {@UniqueConstraint(columnNames = {"channel_id", "global_sku_id"})})
public class ChannelListingPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @Column(name = "channel_sku_id", nullable = false)
    private String channelSkuId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "global_sku_id", nullable = false)
    private Long globalSkuId;
}
