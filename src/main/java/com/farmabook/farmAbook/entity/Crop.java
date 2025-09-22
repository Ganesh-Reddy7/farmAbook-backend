package com.farmabook.farmAbook.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

@Entity
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            // Crop name
    private Double area;            // Area to grow
    private LocalDate date;   // FY e.g., "2025-2026"

    private Double totalInvestment = 0.0;    // Total investment for this crop
    private Double remainingInvestment = 0.0; // Unpaid amount for workers
    private Double totalReturns = 0.0;        // Total returns for this crop

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnEntry> returnEntries = new ArrayList<>();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public LocalDate getDate() { return date; }           // ← getter
    public void setDate(LocalDate date) { this.date = date; } // ← setter

    public Double getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(Double totalInvestment) { this.totalInvestment = totalInvestment; }

    public Double getRemainingInvestment() { return remainingInvestment; }
    public void setRemainingInvestment(Double remainingInvestment) { this.remainingInvestment = remainingInvestment; }

    public Double getTotalReturns() { return totalReturns; }
    public void setTotalReturns(Double totalReturns) { this.totalReturns = totalReturns; }

    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }

    public List<Investment> getInvestments() { return investments; }
    public void setInvestments(List<Investment> investments) { this.investments = investments; }
}
