package io.github.opendme.server.controller;

import io.github.opendme.ITBase;
import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class MemberControllerIT extends ITBase {
    Long departmentId;

    @BeforeEach
    void setUp() {
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

    private void createDepartment() {
        Member admin = new Member(null, null, "master", null, "valid@mail.com");
        admin = memberRepository.save(admin);
        Department d = new Department(null, "blub", admin);
        departmentId = departmentRepository.save(d).getId();
    }

    private MockHttpServletResponse sendCreateRequestWith(Long departmentId, List<Long> skillSet, String email) throws Exception {
        MemberDto memberDto = new MemberDto(departmentId, "Jon Doe", skillSet, email);
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
