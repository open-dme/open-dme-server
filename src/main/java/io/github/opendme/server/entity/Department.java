package io.github.opendme.server.entity;

import jakarta.persistence.*;

@Entity
public record Department(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        Long id,
        String name,
        @OneToOne(optional = false, orphanRemoval = true)
        @JoinColumn(name = "admin_id", nullable = false)
        Member admin) {
}
