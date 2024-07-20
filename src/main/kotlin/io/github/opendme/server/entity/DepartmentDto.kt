package io.github.opendme.server.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class DepartmentDto(
    @field:NotEmpty
    var name: String? = null,
    @field:NotNull
    var adminId: Long? = null
)
