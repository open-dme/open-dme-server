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
) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(  id = $id   ,   name = $name   ,   admin = ${admin?.id} )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Department

        if (id != other.id) return false
        if (name != other.name) return false
        if (admin?.id != other.admin?.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (admin?.id?.hashCode() ?: 0)
        return result
    }
}
