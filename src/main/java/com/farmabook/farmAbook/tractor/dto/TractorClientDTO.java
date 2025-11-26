package com.farmabook.farmAbook.tractor.dto;

public class TractorClientDTO {
    private Long id;
    private Long farmerId;
    private String name;
    private String phone;
    private String address;
    private String notes;

    private Double totalAmount;        // Total amount earned from activities
    private Double amountReceived;     // Total paid by client
    private Double pendingAmount;      // Remaining
    private Double totalAcresWorked;   // Acres worked
    private Integer totalTrips;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(Double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public Double getTotalAcresWorked() {
        return totalAcresWorked;
    }

    public void setTotalAcresWorked(Double totalAcresWorked) {
        this.totalAcresWorked = totalAcresWorked;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }
}
