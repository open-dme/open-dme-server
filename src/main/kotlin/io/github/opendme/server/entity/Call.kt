package io.github.opendme.server.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import java.util.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
class Call {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    var createdAt: Date? = null

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    var department: Department? = null

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    var vehicles: List<Vehicle>? = null

    @OneToMany(mappedBy = "call", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    var callResponses: List<CallResponse>? = null

    constructor()

    constructor(id: Long?, createdAt: Date?, department: Department?, vehicles: List<Vehicle>?) {
        this.id = id
        this.createdAt = createdAt
        this.department = department
        this.vehicles = vehicles
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val call = o as Call
        return id == call.id && createdAt == call.createdAt && department?.id == call.department?.id && vehicles == call.vehicles
    }

    override fun hashCode(): Int {
        return Objects.hash(id, createdAt, department, vehicles)
    }
}
