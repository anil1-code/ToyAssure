package com.increff.dto.helper;

import com.increff.constants.Const;
import com.increff.model.data.ProductData;
import com.increff.model.forms.ProductForm;
import com.increff.pojo.ProductPojo;
import com.increff.util.BasicDataUtil;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoHelper {
    public static List<ProductData> convertPojoListToDataList(List<ProductPojo> productPojoList) {
        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo productPojo : productPojoList) {
            productDataList.add(convertPojoToData(productPojo));
        }
        return productDataList;
    }

    public static ProductData convertPojoToData(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setMrp(productPojo.getMrp());
        productData.setName(productPojo.getName());
        productData.setBrandId(productPojo.getBrandId());
        productData.setDescription(productPojo.getDescription());
        productData.setClientId(productPojo.getClientId());
        productData.setGlobalSkuId(productPojo.getGlobalSkuId());
        productData.setClientSkuId(productPojo.getClientSkuId());
        return productData;
    }

    public static void validateAndNormalise(ProductForm productForm, int row, StringBuilder errorMsg) {
        List<String> errors = new ArrayList<>();
        if (productForm == null) {
            errors.add("Form cannot be null");
        } else {
            if (BasicDataUtil.isEmpty(productForm.getBrandId())) {
                errors.add("Brand ID cannot be null or empty");
            } else if (productForm.getBrandId().length() > Const.MAX_LENGTH) {
                errors.add("Brand ID cannot exceed " + Const.MAX_LENGTH + " chars");
            }

            if (productForm.getClientId() == null) {
                errors.add("Client ID cannot be null");
            }

            if (BasicDataUtil.isEmpty(productForm.getName())) {
                errors.add("Product name cannot be null or empty");
            } else if (productForm.getName().length() > Const.MAX_LENGTH) {
                errors.add("Product name cannot exceed " + Const.MAX_LENGTH + " chars");
            } else {
                productForm.setName(BasicDataUtil.trimAndLowerCase(productForm.getName()));
            }

            if (BasicDataUtil.isEmpty(productForm.getClientSkuId())) {
                errors.add("ClientSkuId cannot be null or empty");
            } else if (productForm.getClientSkuId().length() > Const.MAX_LENGTH) {
                errors.add("ClientSkuId cannot exceed " + Const.MAX_LENGTH + " chars");
            }

            if (productForm.getDescription() != null && productForm.getDescription().length() > Const.MAX_DESCRIPTION_LENGTH) {
                errors.add("Product description cannot exceed " + Const.MAX_DESCRIPTION_LENGTH + " chars");
            }

            if (productForm.getMrp() == null) { // what should be the condition on mrp
                errors.add("Mrp cannot be null");
            } else {
                productForm.setMrp(BasicDataUtil.roundOffDouble(productForm.getMrp()));
                if (productForm.getMrp() < 0) {
                    errors.add("Mrp must be positive");
                }
            }
        }
        if (errors.size() == 0) return;
        if (row != -1) // -1 indicates that the caller function do not want to add this prefix
            errorMsg.append("row ").append(row).append(": ");
        errorMsg.append(errors.get(0));
        for (int i = 1; i < errors.size(); i++) {
            errorMsg.append(", ");
            errorMsg.append(errors.get(i));
        }
        errorMsg.append(".\n");
    }

    public static ProductPojo convertFormToPojo(@NotNull ProductForm productForm) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setDescription(productForm.getDescription());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrandId(productForm.getBrandId());
        productPojo.setClientId(productForm.getClientId());
        productPojo.setName(productForm.getName());
        productPojo.setClientSkuId(productForm.getClientSkuId());
        return productPojo;
    }
}