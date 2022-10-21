package com.increff.controller;

import com.increff.dto.BinDto;
import com.increff.model.data.BinData;
import com.increff.model.forms.BinwiseInventoryForm;
import com.increff.pojo.BinPojo;
import com.increff.pojo.BinSkuPojo;
import exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/bin")
public class BinController {
    @Autowired
    private BinDto binDto;

    @ApiOperation("create n number of bins")
    @RequestMapping(value = "/{binCount}", method = RequestMethod.POST)
    public List<BinPojo> add(@PathVariable Long binCount) throws ApiException {
        return binDto.add(binCount);
    }

    @ApiOperation("get all bins")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BinData> getAll() {
        return binDto.getAll();
    }

    @ApiOperation("put products into the bin")
    @RequestMapping(value = "/binwise_inventory", method = RequestMethod.POST)
    public List<BinSkuPojo> addInventory(@RequestBody List<BinwiseInventoryForm> binwiseInventoryFormList) throws ApiException {
        return binDto.addInventory(binwiseInventoryFormList);
    }
}
