package io.github.opendme.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(nullable = false)
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

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Member admin() {
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
