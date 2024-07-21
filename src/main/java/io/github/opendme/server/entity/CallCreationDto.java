package io.github.opendme.server.entity;

import java.util.List;
import java.util.Objects;

public final class CallCreationDto {
    private final Long departmentId;
    private final List<Long> vehicleIds;

    public CallCreationDto(Long departmentId, List<Long> vehicleIds) {
        this.departmentId = departmentId;
        this.vehicleIds = vehicleIds;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public List<Long> getVehicleIds() {
        return vehicleIds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CallCreationDto) obj;
        return Objects.equals(this.departmentId, that.departmentId) &&
                Objects.equals(this.vehicleIds, that.vehicleIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, vehicleIds);
    }

    @Override
    public String toString() {
        return "CallCreationDto[" +
                "departmentId=" + departmentId + ", " +
                "vehicleIds=" + vehicleIds + ']';
    }

}
