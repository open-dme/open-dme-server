package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record Department(
        @Id
        Long id,
        String name,
        Long adminId) {
}
