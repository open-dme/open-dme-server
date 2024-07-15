package io.github.opendme.server.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public record Member(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false)
        Long id,
        @ManyToOne(optional = false)
        @JoinColumn(nullable = false)
        Department department,
        String name,
        @ManyToMany
        @JoinTable
        Set<Skill> skills,
        String email) {
}
