package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Map;

@Entity
public record Vehicle(
        @Id
        Long id,
        String name,
        Integer seats,
        Map<Skill, Integer> requiredSkills) {
}
