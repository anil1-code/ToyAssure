package com.increff.dao;

import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinDao extends AbstractDao {
    private static final String selectBinPojoById = "select p from BinPojo p where id=:id";
    private static final String selectBinSkusByGlobalSkuIDs = "select p from BinSkuPojo p where globalSkuId=:globalSkuId";
    private static final String selectInventoryPojoByGlobalSkuId = "select p from InventoryPojo p where globalSkuId=:globalSkuId";
    private static final String selectByBinIdAndGlobalSkuId = "select p from BinSkuPojo p where binId=:binId and globalSkuId=:globalSkuId";

    public BinPojo add(BinPojo binPojo) {
        em().persist(binPojo);
        return binPojo;
    }

    public BinPojo getBinPojoById(Long id) {
        TypedQuery<BinPojo> query = getQuery(selectBinPojoById, BinPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BinSkuPojo getBinSkuInsideBin(Long binId, Long globalSkuId) {
        TypedQuery<BinSkuPojo> query = getQuery(selectByBinIdAndGlobalSkuId, BinSkuPojo.class);
        query.setParameter("binId", binId);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    public BinSkuPojo addBinSkuPojo(BinSkuPojo binSkuPojo) {
        em().persist(binSkuPojo);
        return binSkuPojo;
    }

    public InventoryPojo getInventoryPojoByGlobalSkuId(Long globalSkuId) {
        TypedQuery<InventoryPojo> query = getQuery(selectInventoryPojoByGlobalSkuId, InventoryPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    public InventoryPojo addInventoryPojo(InventoryPojo inventoryPojo) {
        em().persist(inventoryPojo);
        return inventoryPojo;
    }

    public List<BinSkuPojo> getBinSkusByGlobalSkuIds(Long globalSkuId) {
        TypedQuery<BinSkuPojo> query = getQuery(selectBinSkusByGlobalSkuIDs, BinSkuPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList();
    }
}
