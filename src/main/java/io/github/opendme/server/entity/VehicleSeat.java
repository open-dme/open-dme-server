package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class VehicleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Vehicle vehicle;
    @OneToOne
    private Skill skill;
    private Integer amount;
    private Integer priority;
    private Boolean mandatory;
}
