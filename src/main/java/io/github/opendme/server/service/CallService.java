package io.github.opendme.server.service;

import io.github.opendme.server.entity.Call;
import io.github.opendme.server.entity.CallRepository;
import io.github.opendme.server.entity.CallResponse;
import io.github.opendme.server.entity.CallResponseRepository;
import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberRepository;
import io.github.opendme.server.entity.Status;
import io.github.opendme.server.entity.Vehicle;
import io.github.opendme.server.entity.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class CallService {
    @Value("${call-service.auto-dispatch-window}")
    Duration AUTO_DISPATCH_WINDOW;
    @Value("${call-service.auto-dispatch-threshold}")
    int AUTO_DISPATCH_THRESHOLD;

    CallRepository callRepository;
    VehicleRepository vehicleRepository;
    DepartmentRepository departmentRepository;
    CallResponseRepository callResponseRepository;
    MemberRepository memberRepository;


    public CallService(CallRepository callRepository, VehicleRepository vehicleRepository, DepartmentRepository departmentRepository, CallResponseRepository callResponseRepository, MemberRepository memberRepository) {
        this.callRepository = callRepository;
        this.vehicleRepository = vehicleRepository;
        this.departmentRepository = departmentRepository;
        this.callResponseRepository = callResponseRepository;
        this.memberRepository = memberRepository;
    }

    public Call createFrom(Long departmentId, List<Long> vehicleIds) {
        List<Vehicle> vehicles = getVehicles(departmentId, vehicleIds);
        Department department = getDepartment(departmentId);
        return callRepository.save(new Call(null, LocalDateTime.now(), department, vehicles));
    }

    public void createResponse(Long callId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new HttpClientErrorException(HttpStatusCode.valueOf(422), "Could not fetch member."));
        Call call = callRepository.findById(callId).orElseThrow(() ->
                new HttpClientErrorException(HttpStatusCode.valueOf(422), "Could not fetch call."));

        if (member.getDepartment() == null || !member.getDepartment().equals(call.getDepartment()))
            throw new HttpClientErrorException(HttpStatusCode.valueOf(422), "Call is not from member department.");

        callResponseRepository.save(new CallResponse(null, LocalDateTime.now(), member, call));

        member.setStatus(Status.DISPATCHED);
        memberRepository.save(member);
    }

    public void createResponse(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new HttpClientErrorException(HttpStatusCode.valueOf(422), "Could not fetch member."));

        callResponseRepository.save(new CallResponse(null, LocalDateTime.now(), member, null));

        member.setStatus(Status.DISPATCHED);
        memberRepository.save(member);

        createCallIfEnoughResponses(member.getDepartment().getId());
    }

    private void createCallIfEnoughResponses(Long departmentId) {
        LocalDateTime considerResponsesBefore = LocalDateTime.now().minus(AUTO_DISPATCH_WINDOW);
        List<CallResponse> responses = callResponseRepository.findAllByMemberDepartmentIdAndCreatedAtAfterAndCallNull(departmentId, considerResponsesBefore);

        if (responses.size() >= AUTO_DISPATCH_THRESHOLD) {
            Call call = createFrom(departmentId, null);
            responses.forEach(r -> {
                r.setCall(call);
                callResponseRepository.save(r);
            });
        }
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
