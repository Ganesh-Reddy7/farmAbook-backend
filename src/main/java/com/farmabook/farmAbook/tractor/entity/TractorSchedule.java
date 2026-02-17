package com.farmabook.farmAbook.tractor.entity;

import com.farmabook.farmAbook.tractor.enums.AreaUnit;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.farmabook.farmAbook.tractor.enums.TractorScheduleStatus;


@Data
@Entity
@Table(name = "tractor_schedule")
public class TractorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tractorId;

    private Long farmerId;

    private Long clientId;

    private Long fieldId;

    private String jobType;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal area;   // original value entered

    @Enumerated(EnumType.STRING)
    private AreaUnit areaUnit;

    @Column(name = "area_in_acres", precision = 10, scale = 4, nullable = false)
    private BigDecimal areaInAcres;

    @Column(precision = 12, scale = 2)
    private BigDecimal expectedJobCost;

    @Column(precision = 10, scale = 2)
    private BigDecimal expectedFuelLitres;

    // ₹ expected fuel cost
    @Column(precision = 12, scale = 2)
    private BigDecimal expectedFuelCost;

    @Column(name = "schedule_datetime")
    private LocalDateTime scheduleDateTime;

    @Enumerated(EnumType.STRING)
    private TractorScheduleStatus status;

    private Integer priorityOrder;

    @Column(name = "client_display_name", length = 100)
    private String clientDisplayName;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) status = TractorScheduleStatus.PENDING;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
