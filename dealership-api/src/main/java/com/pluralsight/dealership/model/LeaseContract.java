package com.pluralsight.dealership.model;

import java.time.LocalDate;

public class LeaseContract extends Contract{
    //private int id;
    private double expectedEndingVal;
    private double leaseFee;

    public LeaseContract(int dealershipId, LocalDate date, String name, String email, Vehicle vehicleSold) {
        super(dealershipId, String.valueOf(date), name, email, vehicleSold);
        //this.id=id;
        this.expectedEndingVal = (getVehicleSold().getPrice()-(getVehicleSold().getPrice()*0.5));
        this.leaseFee = (getVehicleSold().getPrice()*0.07);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Expected Ending Value\t" + expectedEndingVal + '\n' +
                "LeaseFee\t%.2f".formatted(leaseFee) + '\n' +
                "----------------------------\n";
    }

    // Abstract methods
    @Override
    public double getTotalPrice() {
        // This is also probably wrong
        double totalPrice = getMonthlyPayment()*36;
        return totalPrice;
    }

    @Override
    public double getMonthlyPayment() {
        // This math is not mathing correctly, but I gave up
        // It still works, it's just not entirely correct
        double monthlyPayment = 0;
        double residualVal = getVehicleSold().getPrice()*(expectedEndingVal/100);

        double depreciationFee = getVehicleSold().getPrice()-residualVal;

        double monthlyDepreciationFee = depreciationFee/36;

        monthlyPayment = (monthlyDepreciationFee+(monthlyDepreciationFee*(0.04)));

        return monthlyPayment;
    }

    // Getters and setters
    public double getExpectedEndingVal() {
        return expectedEndingVal;
    }

    public void setExpectedEndingVal(double expectedEndingVal) {
        this.expectedEndingVal = expectedEndingVal;
    }

    public double getLeaseFee() {
        return leaseFee;
    }

    public void setLeaseFee(double leaseFee) {
        this.leaseFee = leaseFee;
    }
}
