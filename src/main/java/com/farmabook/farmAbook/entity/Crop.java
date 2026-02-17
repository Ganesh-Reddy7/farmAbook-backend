package com.farmabook.farmAbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Crop {

    // Getters & Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            // Crop name
    private Double area;            // Area to grow
    // ← setter
    // ← getter
    private LocalDate date;   // FY e.g., "2025-2026"

    private Double totalInvestment = 0.0;    // Total investment for this crop
    private Double remainingInvestment = 0.0; // Unpaid amount for workers
    private Double totalReturns = 0.0;
    private Double totalProduction = 0.0;   // NEW → production (yield) for this crop
// Total returns for this crop

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnEntry> returnEntries = new ArrayList<>();

}
