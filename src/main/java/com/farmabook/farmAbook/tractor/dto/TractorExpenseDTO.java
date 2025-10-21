package com.farmabook.farmAbook.tractor.dto;

import java.time.LocalDate;

public class TractorExpenseDTO {
    private Long id;
    private Long tractorId;
    private LocalDate expenseDate;
    private String type;
    private Double litres;
    private Double cost;
    private String notes;
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

    public Long getTractorId() { return tractorId; }
    public void setTractorId(Long tractorId) { this.tractorId = tractorId; }

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
