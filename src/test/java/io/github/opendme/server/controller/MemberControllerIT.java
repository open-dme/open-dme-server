package io.github.opendme.server.controller;

import io.github.opendme.ITBase;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.Skill;
import io.github.opendme.server.service.DepartmentService;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class MemberControllerIT extends ITBase {
    Long departmentId;
    @Autowired
    DepartmentService departmentService;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeEach
    void setUp() {
        memberRepository.removeAllDepartments();
        departmentRepository.deleteAll();
        memberRepository.deleteAll();
        skillRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void should_create_minimal_member() throws Exception {
        MockHttpServletResponse response = sendCreateRequestWith(null, null, "valid@mail.com");

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Jon Doe");
    }

    @Test
    @WithMockUser
    void should_create_member_with_department() throws Exception {
        createDepartment();

        MockHttpServletResponse response = sendCreateRequestWith(departmentId, null, "valid@mail.com");

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Jon Doe");
        assertThat(response.getContentAsString()).contains(departmentId.toString());
    }

    @Test
    @WithMockUser
    void should_reject_invalid_department() throws Exception {
        createDepartment();

        MockHttpServletResponse response = sendCreateRequestWith(666L, null, "valid@mail.com");

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    @WithMockUser
    void should_create_member_with_all() throws Exception {
        createDepartment();
        List<Long> skills = createSkills();

        MockHttpServletResponse response = sendCreateRequestWith(departmentId, skills, "valid@mail.com");

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Jon Doe");
        assertThat(response.getContentAsString()).contains(departmentId.toString());
        assertThat(response.getContentAsString()).contains("Atemschutzträger");
    }

    @Test
    @WithMockUser
    void should_reject_invalid_skill() throws Exception {
        createSkills();

        MockHttpServletResponse response = sendCreateRequestWith(null, List.of(9L), "valid@mail.com");

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    @WithMockUser
    void should_reject_invalid_email() throws Exception {
        MockHttpServletResponse response = sendCreateRequestWith(null, null, "notValid.com");

        assertThat(response.getStatus()).isEqualTo(400);
    }

    private void createDepartment() {
        Member admin = new Member(null, null, "master", null, "valid@mail.com");
        admin = memberRepository.save(admin);
        departmentId = departmentService
                .create(new DepartmentDto("blub", admin.getId()))
                .getId();
    }

    private List<Long> createSkills() {
        List<Long> skillIds = new ArrayList<>();
        skillIds.add(skillRepository.save(new Skill(null, "Fahrer")).getId());
        skillIds.add(skillRepository.save(new Skill(null, "Atemschutzträger")).getId());
        return skillIds;
    }

    private MockHttpServletResponse sendCreateRequestWith(Long departmentId, List<Long> skills, String email) throws Exception {
        MemberDto memberDto = new MemberDto(departmentId, "Jon Doe", skills, email);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(memberDto);

        return mvc.perform(
                          post("/member")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf())
                                  .content(requestJson))
                  .andReturn()
                  .getResponse();
    }
}
