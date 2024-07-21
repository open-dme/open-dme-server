package io.github.opendme.server.entity

import jakarta.persistence.*

@Entity
data class MemberSkill(
    @Id @GeneratedValue
    var id: Long? = null,

    @ManyToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    @ManyToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "skill_id")
    var skill: Skill? = null
)
