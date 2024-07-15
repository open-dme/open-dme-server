@file:JvmName("Departement")
package io.github.opendme.server.entity

import jakarta.persistence.*


@Entity
data class Department(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(nullable = false)
    var id: Long? = null,
    var name: String? = null,
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(nullable = false)
    var admin: Member? = null
)
