package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public record Department(
        @Id @GeneratedValue
        Long id,
        String name,
        Long adminId) {
}
