package io.github.opendme.server.controller;

import io.github.opendme.ITBase;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.Permission;
import io.github.opendme.server.entity.Status;
import io.github.opendme.server.service.MemberService;
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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

public class MemberControllerPatchIT extends ITBase {
    Long memberId;
    @Autowired
    MemberService memberService;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();

        createMember();
    }

    @Test
    @WithMockUser
    void should_patch_permission() throws Exception {
        MockHttpServletResponse response = sendPatchPermissionRequestWith(Set.of(Permission.CREATE_USER), memberId);

        assertThat(response.getStatus()).isEqualTo(202);
        assertThat(memberRepository.findById(memberId).get().getPermissions()).isEqualTo(Set.of(Permission.CREATE_USER));
    }

    @Test
    @WithMockUser
    void should_fail_on_wrong_permission() throws Exception {
        MockHttpServletResponse response = sendPatchPermissionRequestWith("QUATSCH", memberId);

        assertThat(response.getStatus()).isEqualTo(400);
    }


    @Test
    @WithMockUser
    void should_patch_status() throws Exception {
        MockHttpServletResponse response = sendPatchStatusRequestWith(Status.AVAILABLE, memberId);

        assertThat(response.getStatus()).isEqualTo(202);
        assertThat(memberRepository.findById(memberId).get().getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    @WithMockUser
    void should_fail_on_wrong_memberId() throws Exception {
        MockHttpServletResponse response = sendPatchStatusRequestWith(Status.AVAILABLE, 99L);

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void should_fail_on_wrong_status() throws Exception {
        MockHttpServletResponse response = sendPatchStatusRequestWith("QUATSCH", memberId);

        assertThat(response.getStatus()).isEqualTo(400);
    }

    private void createMember() {
        memberId = memberService.create(new MemberDto(null, "noob", "valid@mail.com")).getId();
    }

    private MockHttpServletResponse sendPatchStatusRequestWith(Object status, Long memberId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(status);

        return mvc.perform(
                          patch("/member/{memberId}/status", memberId)
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf())
                                  .content(requestJson))
                  .andReturn()
                  .getResponse();
    }

    private MockHttpServletResponse sendPatchPermissionRequestWith(Object status, Long memberId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(status);

        return mvc.perform(
                          patch("/member/{memberId}/permission", memberId)
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf())
                                  .content(requestJson))
                  .andReturn()
                  .getResponse();
    }
}
