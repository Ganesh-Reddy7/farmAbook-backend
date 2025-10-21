package com.farmabook.farmAbook.tractor.entity;

import com.farmabook.farmAbook.entity.Farmer;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tractor")
public class Tractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    private String model;
    private String make;

    @Column(name = "capacity_hp")
    private Integer capacityHp;

    @Column(name = "tractor_status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @OneToMany(mappedBy = "tractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TractorActivity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "tractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TractorExpense> expenses = new ArrayList<>();

    // ✅ No-arg constructor
    public Tractor() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public Integer getCapacityHp() { return capacityHp; }
    public void setCapacityHp(Integer capacityHp) { this.capacityHp = capacityHp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }

    public List<TractorActivity> getActivities() { return activities; }
    public void setActivities(List<TractorActivity> activities) { this.activities = activities; }

    public List<TractorExpense> getExpenses() { return expenses; }
    public void setExpenses(List<TractorExpense> expenses) { this.expenses = expenses; }
}
