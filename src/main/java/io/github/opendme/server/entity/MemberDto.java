package io.github.opendme.server.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link Member}
 */
public class MemberDto implements Serializable {
    private final Long id;
    private final Long departmentId;
    private final String name;
    private final Set<Long> skillIds;
    private final String email;

    public MemberDto(Long id, Long departmentId, String name, Set<Long> skillIds, String email) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.skillIds = skillIds;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public Set<Long> getSkillIds() {
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
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.departmentId, entity.departmentId) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.skillIds, entity.skillIds) &&
                Objects.equals(this.email, entity.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentId, name, skillIds, email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "departmentId = " + departmentId + ", " +
                "name = " + name + ", " +
                "skillIds = " + skillIds + ", " +
                "email = " + email + ")";
    }
}
