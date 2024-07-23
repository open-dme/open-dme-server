package io.github.opendme.server.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
class CallResponse(
    @Id
    @GeneratedValue
    var id: Long? = null,
    @Column(nullable = false)
    var createdAt: LocalDateTime? = null,
    @ManyToOne(optional = false)
    var member: Member? = null,
    @ManyToOne(cascade = [CascadeType.REMOVE], optional = false)
    var call: Call? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CallResponse

        if (id != other.id) return false
        if (createdAt != other.createdAt) return false
        if (member?.id != other.member?.id) return false
        if (call?.id != other.call?.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (member?.id?.hashCode() ?: 0)
        result = 31 * result + (call?.id?.hashCode() ?: 0)
        return result
    }
}
