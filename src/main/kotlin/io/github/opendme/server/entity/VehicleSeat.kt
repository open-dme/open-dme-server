package io.github.opendme.server.entity

import jakarta.persistence.*

@Entity
data class VehicleSeat(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @jakarta.persistence.OneToOne
    var vehicle: Vehicle? = null,

    @OneToOne
    var skill: Skill? = null,
    var amount: Int? = null,
    var priority: Int? = null,
    var mandatory: Boolean? = null
)
