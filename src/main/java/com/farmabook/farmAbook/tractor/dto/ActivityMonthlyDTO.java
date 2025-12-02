package com.farmabook.farmAbook.tractor.dto;
import lombok.Data;


@Data
public class ActivityMonthlyDTO {
    private String month;
    private double total;
    private double received;
    private double remaining;
    private double acres;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public double getAcres() {
        return acres;
    }

    public void setAcres(double acres) {
        this.acres = acres;
    }
}
