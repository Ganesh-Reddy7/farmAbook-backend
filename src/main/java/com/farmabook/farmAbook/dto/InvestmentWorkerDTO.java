package com.farmabook.farmAbook.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InvestmentWorkerDTO {

    @NotBlank
    private String description;

    @NotBlank
    private String date; // "yyyy-MM-dd"

    @NotNull
    private Long farmerId;

    private List<WorkerDTO> workers;

    // Getters & Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public List<WorkerDTO> getWorkers() { return workers; }
    public void setWorkers(List<WorkerDTO> workers) { this.workers = workers; }
}
