package io.github.opendme.server.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MemberSkill {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "skill_id")
    Skill skill;
}
