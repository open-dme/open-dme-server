package io.github.opendme.server.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@SuppressWarnings("unused")
public class Department {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    private String name;

    @OneToOne(optional = false, orphanRemoval = true)
    private Member admin;

    public Department() {
    }

    public Department(
            Long id,
            String name,
            Member admin) {
        this.id = id;
        this.name = name;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Member getAdmin() {
        return admin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Department) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.admin, that.admin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, admin);
    }

    @Override
    public String toString() {
        return "Department[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "admin=" + admin + ']';
    }

}
