package io.github.opendme.server.controller;

import io.github.opendme.ITBase;
import io.github.opendme.server.entity.Call;
import io.github.opendme.server.entity.CallCreationDto;
import io.github.opendme.server.entity.CallResponse;
import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.Status;
import io.github.opendme.server.entity.Vehicle;
import io.github.opendme.server.service.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class CallControllerIT extends ITBase {
    private static final Logger log = LogManager.getLogger(CallControllerIT.class);
    private Department department;
    private List<Vehicle> vehicles = new ArrayList<>();
    private Call call;
    private Member member;
    ObjectMapper mapper;
    ObjectWriter ow;
    @Autowired
    private DepartmentService departmentService;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeEach
    void setUp() {
        callResponseRepository.deleteAll();
        callRepository.deleteAll();
        vehicleRepository.deleteAll();
        memberRepository.removeAllDepartments();
        departmentRepository.deleteAll();
        memberRepository.deleteAll();

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void should_create_call_by_department() throws Exception {
        createDepartment();
        createVehicle();

        MockHttpServletResponse response = sendCreateRequestWith(new CallCreationDto(department.getId(), null));

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains(department.getName());
        assertThat(response.getContentAsString()).contains(vehicles.getFirst().getName());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void should_create_call_by_vehicle() throws Exception {
        createDepartment();
        createVehicle();
        createVehicle();

        MockHttpServletResponse response = sendCreateRequestWith(new CallCreationDto(department.getId(), vehicles.stream().map(Vehicle::getId).toList()));

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains(department.getName());
        assertThat(response.getContentAsString()).contains(vehicles.stream().map(Vehicle::getName).toList());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void should_create_call_by_one_vehicle() throws Exception {
        createDepartment();
        createVehicle();
        createVehicle();

        MockHttpServletResponse response = sendCreateRequestWith(new CallCreationDto(department.getId(), List.of(vehicles.getFirst().getId())));

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains(department.getName());
        assertThat(response.getContentAsString()).contains(vehicles.getFirst().getName());
        assertThat(response.getContentAsString()).doesNotContain(vehicles.getLast().getName());
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void should_fails_on_wrong_department() throws Exception {
        createDepartment();

        MockHttpServletResponse response = sendCreateRequestWith(new CallCreationDto(66L, null));

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void should_fails_on_wrong_vehicle() throws Exception {
        createDepartment();
        createVehicle();

        MockHttpServletResponse response = sendCreateRequestWith(new CallCreationDto(department.getId(), List.of(66L)));

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    @WithMockUser(roles = {"admin"})
    void should_fails_on_vehicle_from_wrong_department() throws Exception {
        createDepartment();
        createVehicle();    //Creates a vehicle for the first department
        createDepartment(); //Overrides the variable department with a new one

        MockHttpServletResponse response = sendCreateRequestWith(new CallCreationDto(department.getId(), vehicles.stream().map(Vehicle::getId).toList()));

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    @WithMockUser
    void should_create_call_response() throws Exception {
        createCall();
        createMember();

        MockHttpServletResponse response = sendCreateResponseRequestWith(call.getId(), member.getId());

        assertThat(response.getStatus()).isEqualTo(201);
        List<CallResponse> allResponse = callResponseRepository.findAll();
        assertThat(allResponse).hasSize(1);
        var first = allResponse.getFirst();
        assertThat(first.getMember().getId()).isEqualTo(member.getId());

        var firstCall = first.getCall();

        assertThat(firstCall).usingRecursiveComparison()
                             .ignoringFields("createdAt", "callResponses")
                             .isEqualTo(call);

        assertThat(firstCall.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(10, ChronoUnit.SECONDS));
    }

    @Test
    @WithMockUser
    void should_set_dispatched_state_on_create_call_response() throws Exception {
        createCall();
        createMember();

        MockHttpServletResponse response = sendCreateResponseRequestWith(call.getId(), member.getId());

        assertThat(response.getStatus()).isEqualTo(201);
        Member savedMember = memberRepository.findById(member.getId()).get();
        assertThat(savedMember.getStatus()).isEqualTo(Status.DISPATCHED);
    }

    @Test
    @WithMockUser
    void should_create_a_response_without_a_call() throws Exception {
        createDepartment();
        createMember();

        MockHttpServletResponse response = sendCreateResponseRequestWith(member.getId());

        assertThat(response.getStatus()).isEqualTo(201);
        List<CallResponse> allResponse = callResponseRepository.findAll();
        assertThat(allResponse).hasSize(1);
        var first = allResponse.getFirst();
        assertThat(first.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @WithMockUser
    void should_create_a_call_on_many_responses_without_a_call() throws Exception {
        createDepartment();
        createVehicle();
        createMember();
        sendCreateResponseRequestWith(member.getId());
        createMember();
        sendCreateResponseRequestWith(member.getId());
        createMember();
        sendCreateResponseRequestWith(member.getId());

        List<Call> calls = callRepository.findAll();
        assertThat(calls).hasSize(1);
        List<CallResponse> allResponse = callResponseRepository.findAll();
        assertThat(allResponse).hasSize(3);
        assertThat(allResponse).allMatch(r -> r.getCall().getId().equals(calls.getFirst().getId()));
    }

    private void createCall() {
        createDepartment();
        createVehicle();
        call = callRepository.save(new Call(null, LocalDateTime.now(), department, vehicles));
    }

    private void createDepartment() {
        Member admin = new Member(null, null, randomAlphanumeric(8), null, "valid@mail.com");
        admin = memberRepository.save(admin);
        department = departmentService
                .create(new DepartmentDto("blub", admin.getId()));
    }

    private void createVehicle() {
        vehicles.add(vehicleRepository
                .save(new Vehicle(null, randomAlphanumeric(8), 6, department)));
    }

    private void createMember() {
        member = memberRepository
                .save(new Member(null, department, "Bob", null, "blub@mail.com"));
    }

    private MockHttpServletResponse sendCreateRequestWith(CallCreationDto dto) throws Exception {
        return mvc.perform(
                          post("/call")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf())
                                  .content(ow.writeValueAsString(dto)))
                  .andReturn()
                  .getResponse();
    }

    private MockHttpServletResponse sendCreateResponseRequestWith(Long callId, Long memberId) throws Exception {
        return mvc.perform(
                          post("/call/{callId}/response", callId)
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf())
                                  .content(ow.writeValueAsString(memberId)))
                  .andReturn()
                  .getResponse();
    }

    private MockHttpServletResponse sendCreateResponseRequestWith(Long memberId) throws Exception {
        return mvc.perform(
                          post("/call/response")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf())
                                  .content(ow.writeValueAsString(memberId)))
                  .andReturn()
                  .getResponse();
    }

}
