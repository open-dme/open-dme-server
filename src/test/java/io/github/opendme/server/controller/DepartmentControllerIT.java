package io.github.opendme.server.controller;

import io.github.opendme.ITBase;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DepartmentControllerIT extends ITBase {
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

        DepartmentDto departmentDto = new DepartmentDto("Capitol-Außenstelle", adminId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(departmentDto);

        MvcResult result = mvc.perform(
                                      post("/department")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .with(csrf())
                                              .content(requestJson))
                              .andExpect(status().isOk())
                              .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains("Capitol-Außenstelle");
    }
}
