package io.github.opendme.server.entity

import jakarta.persistence.*

@Entity
data class Vehicle(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(nullable = false)
    var id: Long? = null,
    var name: String? = null,
    var seats: Int? = null
)
