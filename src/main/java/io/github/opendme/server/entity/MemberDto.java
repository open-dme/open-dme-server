package io.github.opendme.server.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link Member}
 */
@Schema
public class MemberDto implements Serializable {
    private final Long departmentId;
    @Schema(description = "the name", example = "test")
    @NotEmpty(message = "Name can not be empty")
    private final String name;
    @Schema(description = "the email", example = "test@test.de")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email must be valid")
    private final String email;

    public MemberDto(Long departmentId, String name, String email) {
        this.departmentId = departmentId;
        this.name = name;
        this.email = email;
    }


    public Long getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto entity = (MemberDto) o;
        return Objects.equals(this.departmentId, entity.departmentId) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.email, entity.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, name, email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "departmentId = " + departmentId + ", " +
                "name = " + name + ", " +
                "email = " + email + ")";
    }
}
