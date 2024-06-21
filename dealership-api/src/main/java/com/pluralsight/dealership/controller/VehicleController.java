package com.pluralsight.dealership.controller;

import com.pluralsight.dealership.dao.VehicleDao;
import com.pluralsight.dealership.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {
    private VehicleDao vehicleDao;

    @Autowired
    public VehicleController(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    @RequestMapping(path="/vehicles",method=RequestMethod.GET)
    public List<Vehicle> getVehicle(@RequestParam("minPrice") Double minPrice, @RequestParam("maxPrice") Double maxPrice,
                                    @RequestParam("make") String make, @RequestParam("model") String model,
                                    @RequestParam("minYear") Integer minYear, @RequestParam("maxYear") Integer maxYear,
                                    @RequestParam("color") String color, @RequestParam("minMiles") Integer minMiles,
                                    @RequestParam("maxMiles") Integer maxMiles, @RequestParam("type") String type){

        return vehicleDao.getVehicle(minPrice,maxPrice,make,model,minYear,maxYear,color,minMiles,maxMiles,type);
        // Search with localhost:8080/vehicles?minPrice=0&maxPrice=20000&make=Honda&model=Accord&minYear=2010&maxYear=2024&color=Silver&minMiles=0&maxMiles=200000&type=Sedan
    }

    @RequestMapping(path = "/vehicles", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle vehicle){
        Vehicle newVehicle = vehicleDao.addVehicle(vehicle);
        return vehicle;
    }

    @RequestMapping(path="/vehicles/{vin}",method = RequestMethod.PUT)
    public void updateVehicle(@PathVariable int vin, @RequestBody Vehicle vehicle){
        vehicleDao.updateVehicle(vin, vehicle);
    }

    @RequestMapping(path="/vehicles/{vin}",method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable int vin){
        vehicleDao.removeVehicle(vin);
    }
}
