package io.github.opendme.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;
    String name;
    Integer seats;
    @ManyToOne(optional = false)
    private Department department;

    public Vehicle() {
    }

    public Vehicle(Long id, String name, Integer seats, Department department) {
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id) && Objects.equals(name, vehicle.name) && Objects.equals(seats, vehicle.seats) && Objects.equals(department, vehicle.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, seats, department);
    }
}
