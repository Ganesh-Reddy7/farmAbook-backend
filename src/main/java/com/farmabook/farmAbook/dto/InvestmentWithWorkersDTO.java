package com.farmabook.farmAbook.dto;

import java.util.List;

public class InvestmentWithWorkersDTO {
    private Long id;
    private String description;
    private String date;
    private Double amount;
    private Double remainingAmount;
    private Long farmerId;
    private List<WorkerDTO> workers;

    // ✅ New field for crop
    private CropDTO crop;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Double getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(Double remainingAmount) { this.remainingAmount = remainingAmount; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public List<WorkerDTO> getWorkers() { return workers; }
    public void setWorkers(List<WorkerDTO> workers) { this.workers = workers; }

    public CropDTO getCrop() { return crop; }
    public void setCrop(CropDTO crop) { this.crop = crop; }
}

