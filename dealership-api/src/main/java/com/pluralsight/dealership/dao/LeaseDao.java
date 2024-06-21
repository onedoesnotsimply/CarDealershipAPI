package com.pluralsight.dealership.dao;

import com.pluralsight.dealership.model.LeaseContract;
import com.pluralsight.dealership.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class LeaseDao {
    private DataSource dataSource;

    @Autowired
    public LeaseDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<LeaseContract> getLastTenLeaseContracts() {
        List<LeaseContract> leaseContracts = new ArrayList<>();
        String query = "SELECT * FROM lease_contracts ORDER BY lease_date LIMIT 10";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();){

            while (resultSet.next()){
                //int id = resultSet.getInt(1);
                int dealershipId = resultSet.getInt(2);
                LocalDate leaseDate = resultSet.getDate(3).toLocalDate();
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

                Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                LeaseContract leaseContract = new LeaseContract(dealershipId,leaseDate,name,email,vehicle);

                leaseContracts.add(leaseContract);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return leaseContracts;
    }

    public List<LeaseContract> getAllLeaseContracts() {
        List<LeaseContract> leaseContracts = new ArrayList<>();
        String query = "SELECT * FROM lease_contracts ORDER BY lease_date";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();){

            while (resultSet.next()){
                //int id = resultSet.getInt(1);
                int dealershipId = resultSet.getInt(2);
                LocalDate leaseDate = resultSet.getDate(3).toLocalDate();
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

                Vehicle vehicle = new Vehicle(vin,year,make,model,vehicleType,color,odometer,price);
                LeaseContract leaseContract = new LeaseContract(dealershipId,leaseDate,name,email,vehicle);

                leaseContracts.add(leaseContract);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return leaseContracts;
    }

    public void addLeaseContract(int dealershipId, LeaseContract leaseContract) {
        String query1 = "INSERT INTO lease_contracts " +
                "(dealership_id,lease_date,name,email,vin,year,make,model,vehicleType,color,odometer,price,expected_ending_price,lease_fee) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String query2 = "UPDATE vehicles SET sold = 1 WHERE vin = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
             PreparedStatement preparedStatement2 = connection.prepareStatement(query2);) {

            preparedStatement1.setInt(1, dealershipId);
            preparedStatement1.setDate(2, Date.valueOf(leaseContract.getDate()));
            preparedStatement1.setString(3, leaseContract.getName());
            preparedStatement1.setString(4, leaseContract.getEmail());
            preparedStatement1.setInt(5, leaseContract.getVehicleSold().getVin());
            preparedStatement1.setInt(6, leaseContract.getVehicleSold().getYear());
            preparedStatement1.setString(7, leaseContract.getVehicleSold().getMake());
            preparedStatement1.setString(8, leaseContract.getVehicleSold().getModel());
            preparedStatement1.setString(9, leaseContract.getVehicleSold().getVehicleType());
            preparedStatement1.setString(10, leaseContract.getVehicleSold().getColor());
            preparedStatement1.setInt(11, leaseContract.getVehicleSold().getOdometer());
            preparedStatement1.setDouble(12, leaseContract.getVehicleSold().getPrice());
            preparedStatement1.setDouble(13, leaseContract.getExpectedEndingVal());
            preparedStatement1.setDouble(14, leaseContract.getLeaseFee());

            preparedStatement2.setInt(1, leaseContract.getVehicleSold().getVin());

            int rows1 = preparedStatement1.executeUpdate();
            int rows2 = preparedStatement2.executeUpdate();

            if (rows1 > 0 && rows2 > 0) {
                System.out.println("Contract added successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
