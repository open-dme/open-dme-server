package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Objects;

@Entity
public class VehicleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Skill skill;
    private Integer amount;
    private Integer priority;
    private Boolean mandatory;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "vehicle")
    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleSeat that = (VehicleSeat) o;
        return Objects.equals(id, that.id) && Objects.equals(skill, that.skill) && Objects.equals(amount, that.amount) && Objects.equals(priority, that.priority) && Objects.equals(mandatory, that.mandatory) && Objects.equals(vehicle, that.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skill, amount, priority, mandatory, vehicle);
    }
}
