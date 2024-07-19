package io.github.opendme.server.controller;

import io.github.opendme.ITBase;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class DepartmentControllerIT extends ITBase {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void should_create_department_on_call() throws Exception {
        Member admin = new Member(null, null, "Bernd Stromberg", null, "stromberg@schadensregulierung.capitol.de");
        Long adminId = memberRepository.save(admin).getId();

        MockHttpServletResponse response = sendCreateRequestWith(adminId);

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getContentAsString()).contains("Capitol-Außenstelle");
        assertThat(response.getContentAsString()).contains(adminId.toString());
    }

    @Test
    @WithMockUser
    void should_decline_empty_admin() throws Exception {
        MockHttpServletResponse response = sendCreateRequestWith(null);

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void should_decline_non_existing_admin() throws Exception {
        MockHttpServletResponse response = sendCreateRequestWith(666L);

        assertThat(response.getStatus()).isEqualTo(422);
    }

    private MockHttpServletResponse sendCreateRequestWith(Long adminId) throws Exception {
        DepartmentDto departmentDto = new DepartmentDto("Capitol-Außenstelle", adminId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(departmentDto);

        return mvc.perform(
                          post("/department")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf())
                                  .content(requestJson))
                  .andReturn()
                  .getResponse();
    }
}
