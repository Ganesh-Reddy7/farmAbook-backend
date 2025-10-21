package com.farmabook.farmAbook.tractor.dto;

public class TractorDTO {
    private Long id;
    private String serialNumber;
    private String model;
    private String make;
    private Integer capacityHp;
    private String status;
    private Long farmerId; // ✅ added

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public Integer getCapacityHp() { return capacityHp; }
    public void setCapacityHp(Integer capacityHp) { this.capacityHp = capacityHp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
