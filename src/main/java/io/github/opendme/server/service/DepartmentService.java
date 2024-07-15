package io.github.opendme.server.service;

import io.github.opendme.server.entity.Department;
import io.github.opendme.server.repository.DepartmentRepository;
import io.github.opendme.server.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    DepartmentRepository departmentRepository;
    MemberRepository memberRepository;

    public DepartmentService(DepartmentRepository repository, MemberRepository memberRepository) {
        this.departmentRepository = repository;
        this.memberRepository = memberRepository;
    }

    public Department create(Department department) {
        if (adminExists(department))
            return departmentRepository.save(department);
        return null;
    }

    private boolean adminExists(Department department) {
        return memberRepository.existsById(department.adminId());
    }
}
