package io.github.opendme.server.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link Member}
 */
public class MemberDto implements Serializable {
    private final Long departmentId;
    @NotEmpty(message = "Name can not be empty")
    private final String name;
    private final List<Long> skillIds;
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email must be valid")
    private final String email;

    public MemberDto(Long departmentId, String name, List<Long> skillIds, String email) {
        this.departmentId = departmentId;
        this.name = name;
        this.skillIds = skillIds;
        this.email = email;
    }


    public Long getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public List<Long> getSkillIds() {
        return skillIds;
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
                Objects.equals(this.skillIds, entity.skillIds) &&
                Objects.equals(this.email, entity.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, name, skillIds, email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "departmentId = " + departmentId + ", " +
                "name = " + name + ", " +
                "skillIds = " + skillIds + ", " +
                "email = " + email + ")";
    }
}
