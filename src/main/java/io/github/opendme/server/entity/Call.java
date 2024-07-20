package io.github.opendme.server.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Call {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Date createdAt;
    @ManyToOne(fetch = LAZY, cascade = REMOVE)
    private Department department;

    @OneToMany(fetch = LAZY, cascade = REMOVE)
    private List<Vehicle> vehicles;

    public Call(Long id, Date createdAt, Department department, List<Vehicle> vehicles) {
        this.id = id;
        this.createdAt = createdAt;
        this.department = department;
        this.vehicles = vehicles;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return Objects.equals(id, call.id) && Objects.equals(createdAt, call.createdAt) && Objects.equals(department, call.department) && Objects.equals(vehicles, call.vehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, department, vehicles);
    }
}
