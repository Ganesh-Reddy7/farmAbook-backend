package com.farmabook.farmAbook.entity;

import jakarta.persistence.*;

@Entity
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String role;
    private Double wage;
    private boolean paymentDone = false; // ✅ add this field


    @ManyToOne
    @JoinColumn(name = "investment_id")
    private Investment investment;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Double getWage() { return wage; }
    public void setWage(Double wage) { this.wage = wage; }

    public boolean isPaymentDone() { return paymentDone; } // ✅ getter
    public void setPaymentDone(boolean paymentDone) { this.paymentDone = paymentDone; } // ✅ setter

    public Investment getInvestment() { return investment; }
    public void setInvestment(Investment investment) { this.investment = investment; }

    public Boolean getPaymentDone() { return paymentDone; }
//    public void setPaymentDone(Boolean paymentDone) { this.paymentDone = paymentDone; }
}
