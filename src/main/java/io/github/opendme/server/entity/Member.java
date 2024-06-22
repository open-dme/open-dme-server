package io.github.opendme.server.entity;

import java.util.List;

public record Member(Long id, Long departmentId, String name, List<Skill> skills) {
}
