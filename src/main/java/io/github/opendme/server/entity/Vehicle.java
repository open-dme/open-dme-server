package io.github.opendme.server.entity;

import jakarta.persistence.*;

import java.util.Map;

@Entity
public record Vehicle(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false)
        Long id,
        String name,
        Integer seats) {
}
