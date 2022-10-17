package com.increff.dao;

import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ChannelDao extends AbstractDao {
    private static final String selectByName = "select p from ChannelPojo p where name=:name";
    private static final String selectByChannelAndGlobalSkuId = "select p from ChannelListingPojo p where channelId=:channelId and globalSkuId=:globalSkuId";

    public ChannelPojo add(ChannelPojo channelPojo) {
        em().persist(channelPojo);
        return channelPojo;
    }

    public ChannelPojo getByName(String name) {
        TypedQuery<ChannelPojo> typedQuery = getQuery(selectByName, ChannelPojo.class);
        typedQuery.setParameter("name", name);
        return getSingle(typedQuery);
    }

    public ChannelListingPojo addChannelListingPojo(ChannelListingPojo channelListingPojo) {
        em().persist(channelListingPojo);
        return channelListingPojo;
    }

    public ChannelListingPojo getByChannelAndGlobalSkuId(Long channelId, Long globalSkuId) {
        TypedQuery<ChannelListingPojo> query = getQuery(selectByChannelAndGlobalSkuId, ChannelListingPojo.class);
        query.setParameter("channelId", channelId);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }
}
