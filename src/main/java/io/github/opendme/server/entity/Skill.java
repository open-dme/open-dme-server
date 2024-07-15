package io.github.opendme.server.entity;

import jakarta.persistence.*;

@Entity
public record Skill(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false)
        Long id,
        String name) {
}
