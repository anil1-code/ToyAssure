package com.increff.dao;

import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class BinDao extends AbstractDao {

    public BinPojo add(BinPojo binPojo) {
        em().persist(binPojo);
        return binPojo;
    }

    private static final String selectBinPojoById = "select p from BinPojo p where id=:id";
    private static final String selectInventoryPojoByGlobalSkuId = "select p from InventoryPojo p where globalSkuId=:globalSkuId";
    private static final String selectByBinIdAndGlobalSkuId = "select p from BinSkuPojo p where binId=:binId and globalSkuId=:globalSkuId";

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
}
