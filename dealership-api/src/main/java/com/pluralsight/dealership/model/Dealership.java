package com.pluralsight.dealership.model;

import java.util.ArrayList;
import java.util.List;

public class Dealership {
    private int id;
    private String name;
    private String address;
    private String phone;

    ArrayList<Vehicle> inventory;
    ArrayList<Vehicle> searchResults;

    public Dealership(int id, String name, String address, String phone) {
        this.id=id;
        this.name=name;
        this.address=address;
        this.phone=phone;
        inventory = new ArrayList<>();
        searchResults = new ArrayList<>();
    }

    public List<Vehicle> getVehiclesByPrice(double min, double max){
        searchResults.clear(); // Clear old search results

        // For each vehicle in inventory
        for (Vehicle vehicle : inventory){
            // If the price is within the input range
            if ((vehicle.getPrice() >= min) && (vehicle.getPrice() <= max)){
                // Add it to searchResults
                searchResults.add(vehicle);
            }
        }
        return searchResults;
    }

    public List<Vehicle> getVehiclesByMakeModel(String make, String model){
        searchResults.clear(); // Clear old search results

        // For each vehicle in inventory
        for (Vehicle vehicle:inventory){
            // If the make and model match
            if (make.equalsIgnoreCase(vehicle.getMake()) && model.equalsIgnoreCase(vehicle.getModel())) {
                searchResults.add(vehicle);
            }
        }
        return searchResults;
    }

    public List<Vehicle> getVehiclesByYear(int min, int max) {
        searchResults.clear(); // Clear old search results

        // Loop through inventory
        for (Vehicle vehicle:inventory){
            // If the year is within the range
            if (vehicle.getYear() >= min && vehicle.getYear() <= max){
                searchResults.add(vehicle);
            }
        }
        return searchResults;
    }

    public List<Vehicle> getVehiclesByColor(String color){
        searchResults.clear(); // Clear old search results

        // Loop through inventory
        for (Vehicle vehicle:inventory){
            if (color.equalsIgnoreCase(vehicle.getColor())){
                searchResults.add(vehicle);
            }
        }
        return searchResults;
    }

    public List<Vehicle> getVehiclesByMileage(int min, int max) {
        searchResults.clear(); // Clear search results

        // Loop through inventory
        for (Vehicle vehicle:inventory) {
            if (vehicle.getOdometer() >= min && vehicle.getOdometer() <= max){
                searchResults.add(vehicle);
            }
        }
        return searchResults;
    }

    public List<Vehicle> getVehiclesByType(String vehicleType) {
        searchResults.clear(); // Clear search results

        // Loop through the inventory
        for (Vehicle vehicle : inventory) {
            if (vehicleType.equalsIgnoreCase(vehicle.getVehicleType())){
                searchResults.add(vehicle);
            }
        }
        return searchResults;
    }

    public List<Vehicle> getAllVehicles() {
        // Return all Vehicles in the inventory ArrayList
        return inventory;
    }

    public void addVehicle(Vehicle vehicle){
        // Adds a vehicle to the dealership inventory
        inventory.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
        // Removes a vehicle from the dealership inventory
        inventory.remove(vehicle);
    }

    @Override
    public String toString() {
        return "----------------------------\n" +
                "ID\t" + id + '\n' +
                "Name\t" + name + '\n' +
                "Address\t" + address + '\n' +
                "Phone\t" + phone + '\n' +
                "----------------------------\n";
    }

// Getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
