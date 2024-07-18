package io.github.opendme.server.controller;

import io.github.opendme.ITBase;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.Permission;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.type.CollectionType;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

public class MemberControllerGetIT extends ITBase {
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
    void should_get_permission() throws Exception {
        MockHttpServletResponse response = sendGetPermissionRequestWith(memberId);

        ObjectMapper mapper = new ObjectMapper();
        CollectionType type = mapper.getTypeFactory().constructCollectionType(Set.class, Permission.class);
        Set<Permission> permissions = mapper.readValue(response.getContentAsString(), type);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(permissions).isEqualTo(Set.of(Permission.CREATE_USER));
    }

    @Test
    @WithMockUser
    void should_fail_on_wrong_member_id() throws Exception {
        MockHttpServletResponse response = sendGetPermissionRequestWith(99L);

        assertThat(response.getStatus()).isEqualTo(404);
    }

    private void createMember() {
        Member member = memberService.create(new MemberDto(null, "noob", "valid@mail.com"));
        member.setPermissions(Set.of(Permission.CREATE_USER));
        memberId = member.getId();
        memberRepository.save(member);
    }

    private MockHttpServletResponse sendGetPermissionRequestWith(Long memberId) throws Exception {
        return mvc.perform(
                          get("/member/{memberId}/permission", memberId)
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .with(csrf()))
                  .andReturn()
                  .getResponse();
    }
}
