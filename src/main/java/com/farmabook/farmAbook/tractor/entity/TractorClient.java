package com.farmabook.farmAbook.tractor.entity;

import com.farmabook.farmAbook.entity.Farmer;
import com.farmabook.farmAbook.tractor.enums.ClientType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tractor_client")
public class TractorClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String address;
    private String notes;
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false)
    private ClientType clientType;

    public TractorClient() {}

}
