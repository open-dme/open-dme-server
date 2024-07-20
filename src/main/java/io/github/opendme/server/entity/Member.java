package io.github.opendme.server.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@SuppressWarnings("unused")
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Department department;
    private String name;
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<MemberSkill> skills;
    private String email;
    @Enumerated
    private Status status;
    private Date awayUntil;


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

    public Status getStatus() {
        return status;
    }

    public Date getAwayUntil() {
        return awayUntil;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAwayUntil(Date awayUntil) {
        this.awayUntil = awayUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(department, member.department) && Objects.equals(name, member.name) && Objects.equals(skills, member.skills) && Objects.equals(email, member.email) && status == member.status && Objects.equals(awayUntil, member.awayUntil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, department, name, skills, email, status, awayUntil);
    }
}
