package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record Skill(
        @Id
        Long id,
        String name) {
}
