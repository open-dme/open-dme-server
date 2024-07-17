package io.github.opendme.server.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    long countAllByIdIn(List<Long> ids);

    Set<Skill> findAllByIdIn(List<Long> ids);
}
