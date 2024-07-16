package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.util.Objects;
import java.util.Set;

@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn
    private Department department;
    private String name;
    @ManyToMany
    @JoinTable
    private Set<Skill> skills;
    private String email;

    public Member() {
    }

    public Member(
            Long id,
            Department department,
            String name,
            Set<Skill> skills,
            String email) {
        this.id = id;
        this.department = department;
        this.name = name;
        this.skills = skills;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public String getName() {
        return name;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Member) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.department, that.department) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.skills, that.skills) &&
                Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, department, name, skills, email);
    }

    @Override
    public String toString() {
        return "Member[" +
                "id=" + id + ", " +
                "department=" + department + ", " +
                "name=" + name + ", " +
                "skills=" + skills + ", " +
                "email=" + email + ']';
    }


}
