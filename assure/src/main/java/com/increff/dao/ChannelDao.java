package com.increff.dao;

import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelDao extends AbstractDao {
    private static final String selectAll = "select p from ChannelPojo p";
    private static final String selectAllChannelMappings = "select p from ChannelListingPojo p";
    private static final String selectByName = "select p from ChannelPojo p where name=:name";
    private static final String selectChannelPojoById = "select p from ChannelPojo p where id=:id";
    private static final String selectByChannelAndGlobalSkuId = "select p from ChannelListingPojo p where channelId=:channelId and globalSkuId=:globalSkuId";
    private static final String selectByClientChannelAndChannelSkuId = "select p from ChannelListingPojo p where clientId=:clientId and channelId=:channelId and channelSkuId=:channelSkuId";

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

    public ChannelListingPojo getByClientChannelAndChannelSkuId(Long clientId, Long channelId, String channelSkuId) {
        TypedQuery<ChannelListingPojo> query = getQuery(selectByClientChannelAndChannelSkuId, ChannelListingPojo.class);
        query.setParameter("clientId", clientId);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        return getSingle(query);
    }

    public ChannelPojo getById(Long channelId) {
        TypedQuery<ChannelPojo> query = getQuery(selectChannelPojoById, ChannelPojo.class);
        query.setParameter("id", channelId);
        return getSingle(query);
    }

    public List<ChannelPojo> getAllChannels() {
        TypedQuery<ChannelPojo> query = getQuery(selectAll, ChannelPojo.class);
        return query.getResultList();
    }

    public List<ChannelListingPojo> getAllChannelMappings() {
        TypedQuery<ChannelListingPojo> query = getQuery(selectAllChannelMappings, ChannelListingPojo.class);
        return query.getResultList();
    }
}
