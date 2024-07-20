package io.github.opendme.server.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
data class Department(
    @Id @GeneratedValue @Column
    var id: Long? = null,
    var name: String? = null,
    @OneToOne(optional = false)
    var admin: Member? = null
)
