package io.github.opendme.server.entity

import jakarta.persistence.*

@Entity
data class Member(
    @Id @GeneratedValue
    var id: Long? = null,
    @ManyToOne
    @JoinColumn
    var department: Department? = null,
    var name: String? = null,
    @ManyToMany
    @JoinTable
    var skills: Set<Skill>? = null,
    var email: String? = null
)
