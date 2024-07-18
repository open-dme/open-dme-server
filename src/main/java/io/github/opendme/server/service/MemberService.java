package io.github.opendme.server.service;

import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.MemberRepository;
import io.github.opendme.server.entity.MemberSkill;
import io.github.opendme.server.entity.MemberSkillRepository;
import io.github.opendme.server.entity.Skill;
import io.github.opendme.server.entity.SkillRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Service
public class MemberService {
    DepartmentRepository departmentRepository;
    MemberRepository memberRepository;

    public MemberService(DepartmentRepository departmentRepository, MemberRepository memberRepository) {
        this.departmentRepository = departmentRepository;
        this.memberRepository = memberRepository;
    }

    public Member create(MemberDto dto) {
        validate(dto);
        return memberRepository.save(mapToEntity(dto));
    }

    private void validate(MemberDto dto) {
        if (dto.getDepartmentId() != null &&
                !departmentRepository.existsById(dto.getDepartmentId()))
            throw new HttpClientErrorException(HttpStatusCode.valueOf(422), "Department does not exist.");
//        if (skillsAreSetAndInvalid(dto))
//            throw new HttpClientErrorException(HttpStatusCode.valueOf(422), "Skills not valid.");
    }

//    private boolean skillsAreSetAndInvalid(MemberDto dto) {
//        return !CollectionUtils.isEmpty(dto.getSkillIds()) &&
//                dto.getSkillIds().size() != skillRepository.countAllByIdIn(dto.getSkillIds());
//    }

    private Member mapToEntity(MemberDto dto) {
        Department department = null;
        Set<MemberSkill> skills = null;

        if (Objects.nonNull(dto.getDepartmentId()))
            department = departmentRepository.findById(dto.getDepartmentId()).get();

        return new Member(null, department, dto.getName(), Collections.emptySet(), dto.getEmail());
    }

}
