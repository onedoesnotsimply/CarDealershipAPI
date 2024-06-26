package com.pluralsight.dealership.dao;

import com.pluralsight.dealership.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleDao {
    private DataSource dataSource;

    @Autowired
    public VehicleDao(DataSource dataSource){
        this.dataSource=dataSource;
    }

    public void updateVehicle(int vin, Vehicle vehicle){
        String query = "UPDATE vehicles SET vin = ?, year = ?, make = ?, model = ?, " +
                "vehicleType = ?, color = ?, odometer = ?, price = ?, sold = ? WHERE vin = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);){

            preparedStatement.setInt(1,vehicle.getVin());
            preparedStatement.setInt(2,vehicle.getYear());
            preparedStatement.setString(3, vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setString(5,vehicle.getVehicleType());
            preparedStatement.setString(6, vehicle.getColor());
            preparedStatement.setInt(7,vehicle.getOdometer());
            preparedStatement.setDouble(8,vehicle.getPrice());
            preparedStatement.setBoolean(9,vehicle.isSold());
            preparedStatement.setInt(10,vin);

            int rows = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vehicle> getVehicle(double minPrice,double maxPrice,String make,String model,int minYear,
                              int maxYear,String color, int minMiles,int maxMiles,String type) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE " +
                "price BETWEEN ? AND ? AND " +
                "make LIKE ? AND model LIKE ? AND " +
                "year BETWEEN ? AND ? AND " +
                "color LIKE ? AND " +
                "odometer BETWEEN ? AND ? AND " +
                "vehicleType LIKE ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setDouble(1,minPrice);
            preparedStatement.setDouble(2,maxPrice);
            preparedStatement.setString(3,make);
            preparedStatement.setString(4,model);
            preparedStatement.setInt(5,minYear);
            preparedStatement.setInt(6,maxYear);
            preparedStatement.setString(7,color);
            preparedStatement.setInt(8,minMiles);
            preparedStatement.setInt(9,maxMiles);
            preparedStatement.setString(10,type);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()){
                    do{
                        int vin = resultSet.getInt(1);
                        int year = resultSet.getInt(2);
                        int odometer = resultSet.getInt(7);
                        double price = resultSet.getDouble(8);

                        Vehicle vehicle = new Vehicle(vin,year,make,model,type,color,odometer,price);
                        vehicles.add(vehicle);
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    public void removeVehicle(int vin) {
        String query1 = "DELETE FROM vehicles WHERE vin = ?";
        String query2 = "DELETE FROM inventory WHERE vin = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);){

            preparedStatement1.setInt(1, vin);
            preparedStatement2.setInt(1, vin);

            int rows1 = preparedStatement1.executeUpdate();
            int rows2 = preparedStatement2.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Vehicle addVehicle(Vehicle vehicle){
        String query = "INSERT INTO vehicles " +
                "(vin, year, make, model, vehicleType, color, odometer, price, sold) " +
                "VALUES (?,?,?,?,?,?,?,?,0)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);){

            preparedStatement.setInt(1,vehicle.getVin());
            preparedStatement.setInt(2,vehicle.getYear());
            preparedStatement.setString(3,vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setString(5, vehicle.getVehicleType());
            preparedStatement.setString(6, vehicle.getColor());
            preparedStatement.setInt(7,vehicle.getOdometer());
            preparedStatement.setDouble(8,vehicle.getPrice());

            int rows = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicle;
    }

    public Vehicle getVehicleByVin(int searchVin) {
        Vehicle vehicle = null;
        String query = "SELECT * FROM vehicles WHERE vin = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);){

            preparedStatement.setInt(1,searchVin);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()){
                    //do {
                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);

                    //} while (resultSet.next());
                } else {
                    System.out.println("No vehicle found");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicle;
    }

    public List<Vehicle> getVehiclesByType(int dealershipId, String searchType){
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND vehicleType LIKE ? AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);){

            preparedStatement.setInt(1,dealershipId);
            preparedStatement.setString(2,searchType);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    do {
                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                        vehicles.add(vehicle);

                    } while (resultSet.next());
                } else {
                    System.out.println("No vehicles found");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    public List<Vehicle> getVehiclesByMileage(int dealershipId, int minMiles, int maxMiles){
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND odometer BETWEEN ? AND ? AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);){

            preparedStatement.setInt(1, dealershipId);
            preparedStatement.setInt(2, minMiles);
            preparedStatement.setInt(3, maxMiles);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()){
                    do {
                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                        vehicles.add(vehicle);

                    } while (resultSet.next());
                } else {
                    System.out.println("No vehicles found");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    public List<Vehicle> getVehiclesByColor(int dealershipId, String searchColor) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND color LIKE ? AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1,dealershipId);
            preparedStatement.setString(2,searchColor);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()){
                    do {
                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                        vehicles.add(vehicle);

                    } while (resultSet.next());
                } else {
                    System.out.println("Found no vehicles of that color");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    public List<Vehicle> getVehiclesByYear(int dealershipId, int minYear, int maxYear) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND year BETWEEN ? AND ? AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setInt(1,dealershipId);
            preparedStatement.setInt(2,minYear);
            preparedStatement.setInt(3,maxYear);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()){
                    do {
                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                        vehicles.add(vehicle);

                    } while (resultSet.next());
                } else {
                    System.out.println("No vehicles found");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }

    public List<Vehicle> getVehiclesByMakeModel(int dealershipId, String searchMake, String searchModel) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND make LIKE ? AND model LIKE ? AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            preparedStatement.setInt(1,dealershipId);
            preparedStatement.setString(2, searchMake);
            preparedStatement.setString(3, searchModel);

            try(ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()){
                    do {
                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                        vehicles.add(vehicle);

                    } while (resultSet.next());
                } else {
                    System.out.println("No vehicles of this make/model found");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }


    public List<Vehicle> getVehiclesByPrice(int dealershipId, double minPrice, double maxPrice) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND price BETWEEN ? AND ? AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, dealershipId);
            preparedStatement.setDouble(2,minPrice);
            preparedStatement.setDouble(3,maxPrice);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()){
                    do {

                        int vin = resultSet.getInt("vin");
                        int year = resultSet.getInt("year");
                        String make = resultSet.getString("make");
                        String model = resultSet.getString("model");
                        String vehicleType = resultSet.getString("vehicleType");
                        String color = resultSet.getString("color");
                        int odometer = resultSet.getInt("odometer");
                        double price = resultSet.getDouble("price");

                        Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                        vehicles.add(vehicle);

                    } while (resultSet.next());
                } else {
                    System.out.println("No vehicles found within that range");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }


    public List<Vehicle> getAllVehicles(int dealershipId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE vin IN " +
                "(SELECT vin FROM inventory WHERE dealership_id = ?) " +
                "AND sold = 0";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);){

            preparedStatement.setInt(1,dealershipId);

            try(ResultSet resultSet = preparedStatement.executeQuery();){
                while(resultSet.next()){
                    int vin = resultSet.getInt("vin");
                    int year = resultSet.getInt("year");
                    String make = resultSet.getString("make");
                    String model = resultSet.getString("model");
                    String vehicleType = resultSet.getString("vehicleType");
                    String color = resultSet.getString("color");
                    int odometer = resultSet.getInt("odometer");
                    double price = resultSet.getDouble("price");

                    Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                    vehicles.add(vehicle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }
}
