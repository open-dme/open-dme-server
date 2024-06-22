package io.github.opendme.server.entity;

import java.util.Map;

public record Vehicle(Long id, String name, Integer seats, Map<Skill, Integer> requiredSkills) {
}
