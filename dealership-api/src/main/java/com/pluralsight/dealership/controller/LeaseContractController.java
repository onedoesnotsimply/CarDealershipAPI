package com.pluralsight.dealership.controller;

import com.pluralsight.dealership.dao.LeaseDao;
import com.pluralsight.dealership.model.Contract;
import com.pluralsight.dealership.model.LeaseContract;
import com.pluralsight.dealership.model.SalesContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LeaseContractController {
    private LeaseDao leaseDao;

    @Autowired
    public LeaseContractController(LeaseDao leaseDao) {
        this.leaseDao = leaseDao;
    }

    @RequestMapping(path = "/leases/{id}",method = RequestMethod.GET)
    public Contract getContract(@PathVariable int id){
        return leaseDao.getLeaseContractById(id);
    }

    @RequestMapping(path="/leases", method=RequestMethod.POST)
    public Contract addContract(@RequestBody LeaseContract leaseContract){
        LeaseContract newLeaseContract = leaseDao.addLeaseContract(leaseContract);
        return leaseContract;
    }
}
