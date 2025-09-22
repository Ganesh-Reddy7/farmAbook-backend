package com.farmabook.farmAbook.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CropDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Double area;

    @NotBlank
    private String date;

    private Long farmerId;

    private Double totalInvestment;
    private Double remainingInvestment;
    private Double totalReturns;

    // Optional: list of investments under this crop
    private List<InvestmentDTO> investments;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public String getDate() { return date; }           // ← getter
    public void setDate(String date) { this.date = date; } // ← setter

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public Double getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(Double totalInvestment) { this.totalInvestment = totalInvestment; }

    public Double getRemainingInvestment() { return remainingInvestment; }
    public void setRemainingInvestment(Double remainingInvestment) { this.remainingInvestment = remainingInvestment; }

    public Double getTotalReturns() { return totalReturns; }
    public void setTotalReturns(Double totalReturns) { this.totalReturns = totalReturns; }

    public List<InvestmentDTO> getInvestments() { return investments; }
    public void setInvestments(List<InvestmentDTO> investments) { this.investments = investments; }
}
