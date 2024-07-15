package io.github.opendme.server.service;

import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.MemberRepository;
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
        createAdminIfNeeded(department);
        return departmentRepository.save(department);
    }

    private void createAdminIfNeeded(Department department) {
        //TODO: does admin already get saved by departmentRepository.save()?
        if (!memberRepository.existsById(department.admin().id()))
            memberRepository.save(department.admin());
    }
}
