package io.github.opendme.server.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findAllByDepartmentIdIs(Long departmentId);

    List<Vehicle> findAllByIdInAndDepartmentIdIs(List<Long> id, Long departmentId);
}
