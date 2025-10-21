package com.farmabook.farmAbook.tractor.entity;

import com.farmabook.farmAbook.entity.Farmer;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tractor_expense")
public class TractorExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tractor_id", nullable = false)
    private Tractor tractor;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @Column(name = "expense_date")
    private LocalDate expenseDate;

    @Column(name = "expense_type")
    private String type;

    private Double litres;
    private Double cost;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // ✅ No-arg constructor
    public TractorExpense() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tractor getTractor() { return tractor; }
    public void setTractor(Tractor tractor) { this.tractor = tractor; }

    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getLitres() { return litres; }
    public void setLitres(Double litres) { this.litres = litres; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
