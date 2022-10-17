package com.increff.controller;

import com.increff.dto.ProductDto;
import com.increff.exception.ApiException;
import com.increff.model.data.ProductData;
import com.increff.model.forms.ProductForm;
import com.increff.pojo.ProductPojo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "gets all the products")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductData> getAll() {
        return productDto.getAll();
    }

    @ApiOperation(value = "add the list of products")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<ProductPojo> add(@RequestBody List<ProductForm> productFormList) throws ApiException {
        return productDto.add(productFormList);
    }

    @ApiOperation(value = "update an existing product")
    @RequestMapping(value = "/update/{globalSkuId}", method = RequestMethod.PUT)
    public ProductPojo update(@PathVariable Long globalSkuId, @RequestBody ProductForm productForm) throws ApiException {
        return productDto.update(globalSkuId, productForm);
    }
}
