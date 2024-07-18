package io.github.opendme.server.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Objects;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@SuppressWarnings("unused")
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Department department;
    private String name;
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<MemberSkill> skills;
    private String email;

    public Member() {
    }

    public Member(
            Long id,
            Department department,
            String name,
            Set<MemberSkill> skills,
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

    public Set<MemberSkill> getSkills() {
        return skills;
    }

    public String getEmail() {
        return email;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkills(Set<MemberSkill> skills) {
        this.skills = skills;
    }

    public void setEmail(String email) {
        this.email = email;
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
