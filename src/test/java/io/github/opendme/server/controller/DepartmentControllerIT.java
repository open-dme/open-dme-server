package io.github.opendme.server.controller;

import io.github.opendme.server.ServerApplicationTests;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class DepartmentControllerIT extends ServerApplicationTests {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
//    @WithMockUser
    void should_create_department_on_call() {
        Member admin = new Member(null, null, "Jon Doe", null, "blazingUnicorns@yourImagination.org");
        Long adminId = memberRepository.save(admin).id();

        DepartmentDto departmentDto = new DepartmentDto(null, "Bloodhound Gang", adminId);
        HttpEntity<DepartmentDto> request = new HttpEntity<>(departmentDto);

        ResponseEntity<String> result = testRestTemplate.postForEntity("/department", request, String.class);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();

    }
}
