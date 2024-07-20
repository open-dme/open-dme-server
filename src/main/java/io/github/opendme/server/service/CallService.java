package io.github.opendme.server.service;

import io.github.opendme.server.entity.Call;
import io.github.opendme.server.entity.CallRepository;
import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.Vehicle;
import io.github.opendme.server.entity.VehicleRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class CallService {
    CallRepository callRepository;
    VehicleRepository vehicleRepository;
    DepartmentRepository departmentRepository;

    public CallService(CallRepository callRepository, VehicleRepository vehicleRepository, DepartmentRepository departmentRepository) {
        this.callRepository = callRepository;
        this.vehicleRepository = vehicleRepository;
        this.departmentRepository = departmentRepository;
    }

    public Call createFrom(Long departmentId, List<Long> vehicleIds) {
        List<Vehicle> vehicles = getVehicles(departmentId, vehicleIds);
        Department department = getDepartment(departmentId);
        return callRepository.save(new Call(null, new Date(), department, vehicles));
    }

    private List<Vehicle> getVehicles(Long departmentId, List<Long> vehicleIds) {
        List<Vehicle> vehicles;
        if (isEmpty(vehicleIds))
            vehicles = vehicleRepository.findAllByDepartmentIdIs(departmentId);
        else
            vehicles = vehicleRepository.findAllByIdInAndDepartmentIdIs(vehicleIds, departmentId);

        if (isEmpty(vehicles))
            throw new HttpClientErrorException(HttpStatusCode.valueOf(422), "Could not fetch vehicle.");
        return vehicles;
    }

    private Department getDepartment(Long departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isEmpty())
            throw new HttpClientErrorException(HttpStatusCode.valueOf(422), "Could not fetch department.");

        return department.get();
    }
}
