package io.github.opendme.server.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSkillRepository extends JpaRepository<MemberSkill, Long> {
    List<MemberSkill> findSkillsByMemberId(Long memberId);

}
