package io.github.opendme.server.controller;

import io.github.opendme.server.ServerApplicationTests;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
class DepartmentControllerIT extends ServerApplicationTests {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
        memberRepository.deleteAll();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser("spring")
    void should_create_department_on_call() throws Exception {
        Member admin = new Member(null, null, "Bernd Stromberg", null, "stromberg@schadensregulierung.capitol.de");
        Long adminId = memberRepository.save(admin).id();

        DepartmentDto departmentDto = new DepartmentDto("Capitol-Außenstelle", adminId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(departmentDto);

        MvcResult result = mvc.perform(post("/department")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(requestJson))
                              .andExpect(status().isOk())
                              .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains("Capitol-Außenstelle");

    }
}
