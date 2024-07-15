package io.github.opendme.server.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link Department}
 */
public class DepartmentDto implements Serializable {
    private final Long id;
    private final String name;
    private final Long adminId;

    public DepartmentDto(Long id, String name, Long adminId) {
        this.id = id;
        this.name = name;
        this.adminId = adminId;
    }

    public Long getId() {
        return id;
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
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.adminId, entity.adminId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, adminId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "adminId = " + adminId + ")";
    }
}
