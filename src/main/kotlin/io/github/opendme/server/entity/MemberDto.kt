package io.github.opendme.server.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Schema
data class MemberDto(
    var departmentId: Long? = null,
    @Schema(description = "the name", example = "test")
    @field:NotBlank(message = "Name cannot be blank")
    var name: String?,

    @Schema(description = "the email", example = "test@test.de")
    @field:Pattern(
        regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
        message = "Email must be valid"
    )
    var email: String?
)
