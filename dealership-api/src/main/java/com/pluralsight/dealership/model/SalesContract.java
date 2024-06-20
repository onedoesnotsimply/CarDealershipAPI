package com.pluralsight.dealership.model;

import java.time.LocalDate;

public class SalesContract extends Contract {
    // ID is auto incremented
    //private int id;
    private double salesTaxAmount;
    private double recordingFee;
    private double processingFee;
    private boolean isFinanced;

    public SalesContract(int dealershipId, LocalDate date, String name, String email, Vehicle vehicleSold, boolean isFinanced) {
        super(dealershipId, String.valueOf(date), name, email, vehicleSold);
        this.salesTaxAmount = 0.05;
        this.recordingFee = 100.0;

        if (vehicleSold.getPrice()<10000){
            this.processingFee = 295;
        } else {
            this.processingFee = 495;
        }

        this.isFinanced = isFinanced;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Financed\t" + isFinanced + '\n' +
                "----------------------------\n";
    }

    // Override methods
    @Override
    public double getTotalPrice() { // The combined cost of everything
        // Initialize price
        double totalPrice = 0;
        // Check if user chose the finance option
        if (isFinanced) { // If they did calculate the total cost with the monthly payment
            totalPrice = (recordingFee+processingFee+getMonthlyPayment())*(salesTaxAmount);
        } else { // If they didn't calculate the total cost with the full cost of the vehicle
            totalPrice = (getVehicleSold().getPrice()+recordingFee+processingFee);
            totalPrice += totalPrice*(salesTaxAmount);
        }

        return totalPrice;
    }

    @Override
    public double getMonthlyPayment() {
        // Initialize monthlyPayment
        double monthlyPayment = 0;
        // Check if the vehicle is financed or not
        if (!isFinanced) {
            return 0; // If the user chose not to take out a loan the monthly payment is 0
        }
        // If the cost of the vehicle is > 10,000
        if (getVehicleSold().getPrice()>10000){
            monthlyPayment = ((getVehicleSold().getPrice()/48)*(4.25/100));
        } else { // < 10,000
            monthlyPayment = ((getVehicleSold().getPrice()/24)*(5.25/100));
        }
        return monthlyPayment;

    }


    // Getters and setters

    public double getSalesTaxAmount() {
        return salesTaxAmount;
    }

    public void setSalesTaxAmount(double salesTaxAmount) {
        this.salesTaxAmount = salesTaxAmount;
    }

    public double getRecordingFee() {
        return recordingFee;
    }

    public void setRecordingFee(double recordingFee) {
        this.recordingFee = recordingFee;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(double processingFee) {
        this.processingFee = processingFee;
    }

    public boolean isFinanced() {
        return isFinanced;
    }

    public void setFinanced(boolean financed) {
        isFinanced = financed;
    }
}
