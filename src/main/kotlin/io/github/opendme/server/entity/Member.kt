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
    @ManyToOne(cascade = [CascadeType.REMOVE])
    var department: Department? = null,
    var name: String? = null,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE])
    var skills: Set<MemberSkill>? = null,
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
        ) : this(id, department, name, skills = skills, email = email, status = null)
}
