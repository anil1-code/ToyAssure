package com.increff.dto;

import com.increff.dto.helper.ProductDtoHelper;
import com.increff.model.data.ProductData;
import com.increff.model.forms.ProductForm;
import com.increff.pojo.ProductPojo;
import com.increff.service.ProductService;
import exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;

    public List<ProductData> getAll() {
        return ProductDtoHelper.convertPojoListToDataList(productService.getAll());
    }

    @Transactional
    public List<ProductPojo> add(List<ProductForm> productFormList) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        for (int i = 0; i < productFormList.size(); i++) {
            ProductDtoHelper.validateAndNormalise(productFormList.get(i), i, errorMsg);
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        List<ProductPojo> productPojoList = new ArrayList<>();
        for (ProductForm productForm : productFormList) {
            ProductDtoHelper.normalize(productForm);
            productPojoList.add(ProductDtoHelper.convertFormToPojo(productForm));
        }
        return productService.add(productPojoList);
    }

    public ProductPojo update(Long globalSkuId, ProductForm productForm) throws ApiException {
        StringBuilder errorMsg = new StringBuilder();
        ProductDtoHelper.validateAndNormalise(productForm, -1, errorMsg);
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        ProductPojo productPojo = ProductDtoHelper.convertFormToPojo(productForm);
        productPojo.setGlobalSkuId(globalSkuId);
        return productService.update(productPojo);
    }
}
