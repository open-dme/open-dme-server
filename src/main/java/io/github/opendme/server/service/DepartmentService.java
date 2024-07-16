package io.github.opendme.server.service;

import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class DepartmentService {
    DepartmentRepository departmentRepository;
    MemberRepository memberRepository;

    public DepartmentService(DepartmentRepository repository, MemberRepository memberRepository) {
        this.departmentRepository = repository;
        this.memberRepository = memberRepository;
    }

    public Department create(DepartmentDto department) {
        return departmentRepository.save(mapToEntity(department));
    }

    Department mapToEntity(DepartmentDto dto) {
        Member admin = fetchAdmin(dto);
        return new Department(null, dto.getName(), admin);
    }

    private Member fetchAdmin(DepartmentDto department) {
        Optional<Member> admin = memberRepository.findById(department.getAdminId());
        if (admin.isEmpty())
            throw new HttpClientErrorException(HttpStatusCode.valueOf(422), "Could not fetch admin.");
        return admin.get();
    }
}
