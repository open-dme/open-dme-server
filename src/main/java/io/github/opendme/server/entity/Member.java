package io.github.opendme.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public record Member(
        @Id
        Long id,
        Long departmentId,
        String name,
        List<Skill> skills) {
}
