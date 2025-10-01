package com.farmabook.farmAbook.loan.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class LoanTransactionDTO {

    private Long id;
    private Long farmerId;
    private String source;
    private Double principal;
    private Double remainingPrincipal;
    private Double amountPaid;
    private Double interestRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double finalInterest;
    private Boolean isClosed;
    private Boolean isGiven;
    private String description;
    private String bondImagePath;
    private Double updatedPrincipal;
    private Double currentInterest;

    // getter and setter
    public Double getCurrentInterest() { return currentInterest; }
    public void setCurrentInterest(Double currentInterest) { this.currentInterest = currentInterest; }


    public Double getUpdatedPrincipal() {
        return updatedPrincipal;
    }

    public void setUpdatedPrincipal(Double updatedPrincipal) {
        this.updatedPrincipal = updatedPrincipal;
    }

    // NEW: file upload
    private MultipartFile bondImageFile;

    // 🔑 NEW FIELDS for maturity compounding
    private Integer maturityPeriodYears;  // how often compounding happens (1 = yearly, 2 = every 2 years, etc.)
    private LocalDate nextMaturityDate;   // next date when compounding applies
    private Boolean nearMaturity;         // flag to notify UI (true if within 30 days of maturity)
    private LocalDate lastCompoundedDate; // tracks last compounding


    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public Double getPrincipal() { return principal; }
    public void setPrincipal(Double principal) { this.principal = principal; }

    public Double getRemainingPrincipal() { return remainingPrincipal; }
    public void setRemainingPrincipal(Double remainingPrincipal) { this.remainingPrincipal = remainingPrincipal; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Double getFinalInterest() { return finalInterest; }
    public void setFinalInterest(Double finalInterest) { this.finalInterest = finalInterest; }

    public Boolean getIsClosed() { return isClosed; }
    public void setIsClosed(Boolean isClosed) { this.isClosed = isClosed; }

    public Boolean getIsGiven() { return isGiven; }
    public void setIsGiven(Boolean isGiven) { this.isGiven = isGiven; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBondImagePath() { return bondImagePath; }
    public void setBondImagePath(String bondImagePath) { this.bondImagePath = bondImagePath; }

    public MultipartFile getBondImageFile() { return bondImageFile; }
    public void setBondImageFile(MultipartFile bondImageFile) { this.bondImageFile = bondImageFile; }

    public Integer getMaturityPeriodYears() { return maturityPeriodYears; }
    public void setMaturityPeriodYears(Integer maturityPeriodYears) { this.maturityPeriodYears = maturityPeriodYears; }

    public LocalDate getNextMaturityDate() { return nextMaturityDate; }
    public void setNextMaturityDate(LocalDate nextMaturityDate) { this.nextMaturityDate = nextMaturityDate; }

    public Boolean getNearMaturity() { return nearMaturity; }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setNearMaturity(Boolean nearMaturity) { this.nearMaturity = nearMaturity; }

    public LocalDate getLastCompoundedDate() {
        return lastCompoundedDate;
    }

    public void setLastCompoundedDate(LocalDate lastCompoundedDate) {
        this.lastCompoundedDate = lastCompoundedDate;
    }
}
