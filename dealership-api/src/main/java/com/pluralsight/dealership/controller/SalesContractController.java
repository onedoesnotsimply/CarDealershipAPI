package com.pluralsight.dealership.controller;

import com.pluralsight.dealership.dao.SalesDao;
import com.pluralsight.dealership.model.Contract;
import com.pluralsight.dealership.model.SalesContract;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SalesContractController {
    private SalesDao salesDao;

    @Autowired
    public SalesContractController(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    @RequestMapping(path = "/sales/{id}",method = RequestMethod.GET)
    public Contract getContract(@PathVariable int id){
        return salesDao.getSalesContractById(id);
    }

    @RequestMapping(path="/sales", method=RequestMethod.POST)
    public Contract addContract(@RequestBody SalesContract salesContract){
        SalesContract newSalesContract = salesDao.addSalesContract(salesContract);
        return salesContract;
    }
}
