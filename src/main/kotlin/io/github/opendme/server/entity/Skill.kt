package io.github.opendme.server.entity

import jakarta.persistence.*

@Entity
data class Skill(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(nullable = false)
    var id: Long? = null,
    var name: String? = null
)
