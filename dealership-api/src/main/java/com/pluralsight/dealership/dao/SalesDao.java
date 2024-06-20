package com.pluralsight.dealership.dao;

import com.pluralsight.dealership.model.SalesContract;
import com.pluralsight.dealership.model.Vehicle;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesDao {
    private DataSource dataSource;

    public SalesDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<SalesContract> getLastTenSalesContracts() {
        List<SalesContract> salesContracts = new ArrayList<>();
        String query = "SELECT * FROM sales_contracts ORDER BY sales_date LIMIT 10";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();){

            while (resultSet.next()){
                //int id = resultSet.getInt(1);
                int dealershipId = resultSet.getInt(2);
                LocalDate salesDate = resultSet.getDate(3).toLocalDate();
                String name = resultSet.getString(4);
                String email = resultSet.getString(5);
                int vin = resultSet.getInt(6);
                int year = resultSet.getInt(7);
                String make = resultSet.getString(8);
                String model = resultSet.getString(9);
                String vehicleType = resultSet.getString(10);
                String color = resultSet.getString(11);
                int odometer = resultSet.getInt(12);
                double price = resultSet.getDouble(13);
                boolean isFinanced = resultSet.getBoolean(14);

                Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                SalesContract salesContract = new SalesContract(dealershipId, salesDate,name,email,vehicle, isFinanced);

                salesContracts.add(salesContract);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salesContracts;
    }

    public List<SalesContract> getAllSalesContracts() {
        List<SalesContract> salesContracts = new ArrayList<>();
        String query = "SELECT * FROM sales_contracts ORDER BY sales_date";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();){

            while (resultSet.next()){
                //int id = resultSet.getInt(1);
                int dealershipId = resultSet.getInt(2);
                LocalDate salesDate = resultSet.getDate(3).toLocalDate();
                String name = resultSet.getString(4);
                String email = resultSet.getString(5);
                int vin = resultSet.getInt(6);
                int year = resultSet.getInt(7);
                String make = resultSet.getString(8);
                String model = resultSet.getString(9);
                String vehicleType = resultSet.getString(10);
                String color = resultSet.getString(11);
                int odometer = resultSet.getInt(12);
                double price = resultSet.getDouble(13);
                boolean isFinanced = resultSet.getBoolean(14);

                Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                SalesContract salesContract = new SalesContract(dealershipId, salesDate,name,email,vehicle, isFinanced);

                salesContracts.add(salesContract);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salesContracts;
    }

    public void addSalesContract(int dealershipId, SalesContract salesContract){
        String query1 = "INSERT INTO sales_contracts " +
                "(dealership_id,sales_date,name,email,vin,year,make,model,vehicleType,color,odometer,price,financed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "UPDATE vehicles SET sold = 1 WHERE vin = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);) {

            preparedStatement1.setInt(1, dealershipId);
            preparedStatement1.setDate(2, Date.valueOf(salesContract.getDate()));
            preparedStatement1.setString(3,salesContract.getName());
            preparedStatement1.setString(4, salesContract.getEmail());
            preparedStatement1.setInt(5,salesContract.getVehicleSold().getVin());
            preparedStatement1.setInt(6,salesContract.getVehicleSold().getYear());
            preparedStatement1.setString(7,salesContract.getVehicleSold().getMake());
            preparedStatement1.setString(8, salesContract.getVehicleSold().getModel());
            preparedStatement1.setString(9,salesContract.getVehicleSold().getVehicleType());
            preparedStatement1.setString(10, salesContract.getVehicleSold().getColor());
            preparedStatement1.setInt(11, salesContract.getVehicleSold().getOdometer());
            preparedStatement1.setDouble(12, salesContract.getVehicleSold().getPrice());
            preparedStatement1.setBoolean(13, salesContract.isFinanced());

            preparedStatement2.setInt(1, salesContract.getVehicleSold().getVin());

            int rows1 = preparedStatement1.executeUpdate();
            int rows2 = preparedStatement2.executeUpdate();

            if (rows1 > 0 && rows2 > 0){
                System.out.println("Contract added successfully");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
