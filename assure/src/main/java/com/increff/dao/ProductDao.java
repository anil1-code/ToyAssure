package com.increff.dao;

import com.increff.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static final String deleteById = "delete from ProductPojo p where id=:id";
    private static final String selectById = "select p from ProductPojo p where globalSkuId=:globalSkuId";
    private static final String selectByBrandCategory = "select p from ProductPojo p where clientId=:clientId and clientSkuId=:clientSkuId";
    private static final String selectAll = "select p from ProductPojo p";

    public List<ProductPojo> getAll() {
        TypedQuery<ProductPojo> tQuery = getQuery(selectAll, ProductPojo.class);
        return tQuery.getResultList();
    }

    public ProductPojo getByClientAndClientSku(Long clientId, String clientSkuId) {
        TypedQuery<ProductPojo> query = getQuery(selectByBrandCategory, ProductPojo.class);
        query.setParameter("clientId", clientId);
        query.setParameter("clientSkuId", clientSkuId);
        return getSingle(query);
    }

    public ProductPojo add(ProductPojo productPojo) {
        em().persist(productPojo);
        return productPojo;
    }

    public ProductPojo getByGlobalSkuId(Long globalSkuId) {
        TypedQuery<ProductPojo> query = getQuery(selectById, ProductPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }
}
