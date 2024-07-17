package io.github.opendme.server.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link Department}
 */
public class DepartmentDto implements Serializable {
    private final String name;
    private final Long adminId;

    public DepartmentDto(String name, Long adminId) {
        this.name = name;
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public Long getAdminId() {
        return adminId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentDto entity = (DepartmentDto) o;
        return Objects.equals(this.name, entity.name) &&
                Objects.equals(this.adminId, entity.adminId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, adminId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "name = " + name + ", " +
                "adminId = " + adminId + ")";
    }
}
