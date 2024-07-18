package io.github.opendme.server.service;

import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.MemberRepository;
import io.github.opendme.server.entity.Skill;
import io.github.opendme.server.entity.SkillRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class MemberServiceTest {
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    SkillRepository skillRepository;
    @InjectMocks
    MemberService service;
    @Captor
    ArgumentCaptor<Member> argumentCaptor;

    static AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        when(memberRepository.save(any())).thenReturn(new Member());
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void should_create_minimal_member() {
        MemberDto dto = new MemberDto(null, null, null);

        Member member = service.create(dto);

        assertThat(member).isNotNull();
    }

    @Test
    @Disabled
    void should_ignore_empty_skills() {
        MemberDto dto = new MemberDto(null, null, null);

        Member member = service.create(dto);

        assertThat(member).isNotNull();
    }

    @Test
    void should_add_department_to_member() {
        MemberDto dto = new MemberDto(1L, null, null);
        when(departmentRepository.existsById(any())).thenReturn(true);
        when(departmentRepository.findById(eq(1L))).thenReturn(Optional.of(new Department(1L, "Hauptamt", new Member())));

        Member member = service.create(dto);

        verify(memberRepository).save(argumentCaptor.capture());
        assertThat(member).isNotNull();
        assertThat(argumentCaptor.getValue().getDepartment().getName()).isEqualTo("Hauptamt");
    }

    @Test
    void should_fail_on_invalid_department() {
        MemberDto dto = new MemberDto(99L, null, null);
        when(departmentRepository.existsById(any())).thenReturn(false);

        assertThrows(HttpClientErrorException.class, () ->
                service.create(dto)
        );
    }

    @Test
    @Disabled
    void should_add_skills_to_member() {
        MemberDto dto = new MemberDto(null, null, null);
        when(skillRepository.countAllByIdIn(anyList())).thenReturn(2L);
        Set<Skill> skills = Set.of(new Skill(1L, "Lesen"), new Skill(1L, "Schreiben"));
        when(skillRepository.findAllByIdIn(anyList())).thenReturn(skills);

        Member member = service.create(dto);

        verify(memberRepository).save(argumentCaptor.capture());
        assertThat(member).isNotNull();
        //assertThat(argumentCaptor.getValue().getSkills()).containsAll(skills);
    }

    @Test
    @Disabled
    void should_fail_on_invalid_skills() {
        MemberDto dto = new MemberDto(null, null, null);
        when(skillRepository.countAllByIdIn(anyList())).thenReturn(0L);

        assertThrows(HttpClientErrorException.class, () ->
                service.create(dto)
        );
    }
}
