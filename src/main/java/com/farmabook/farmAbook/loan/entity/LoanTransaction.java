package com.farmabook.farmAbook.loan.entity;

import com.farmabook.farmAbook.entity.Farmer;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.farmabook.farmAbook.loan.entity.LoanPayment;

@Entity
public class LoanTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    private Double principal;
    private Double remainingPrincipal;
    private Double amountPaid = 0.0;
    private Double interestRate; // per day interest
    private LocalDate startDate;
    private LocalDate endDate;
    private Double finalInterest;
    private Boolean isClosed = false;
    private Boolean isGiven; // true = lent, false = borrowed
    private String description;
    private String bondImagePath;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanPayment> payments = new ArrayList<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }

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

    public Boolean getClosed() { return isClosed; }
    public void setClosed(Boolean closed) { isClosed = closed; }

    public Boolean getGiven() { return isGiven; }
    public void setGiven(Boolean given) { isGiven = given; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBondImagePath() { return bondImagePath; }
    public void setBondImagePath(String bondImagePath) { this.bondImagePath = bondImagePath; }

    public List<LoanPayment> getPayments() { return payments; }
    public void setPayments(List<LoanPayment> payments) { this.payments = payments; }
}
