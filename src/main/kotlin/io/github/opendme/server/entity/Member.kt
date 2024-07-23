package io.github.opendme.server.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import java.util.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
data class Member(
    @Id @GeneratedValue
    var id: Long? = null,
    @ManyToOne
    var department: Department? = null,
    var name: String? = null,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE])
    var skills: Set<MemberSkill>? = emptySet(),
    var email: String? = null,

    @Enumerated
    var status: Status? = null,
    var awayUntil: Date? = null,
) {
    constructor(
        id: Long? = null,
        department: Department? = null,
        name: String? = null,
        skills: Set<MemberSkill>? = emptySet(),
        email: String? = null
    ) : this(id, department, name, skills = skills ?: emptySet(), email = email, status = null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false
        if (department?.id != other.department?.id) return false
        if (name != other.name) return false
        if (skills != other.skills) return false
        if (email != other.email) return false
        if (status != other.status) return false
        if (awayUntil != other.awayUntil) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (department?.id?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (skills?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (awayUntil?.hashCode() ?: 0)
        return result
    }
}
