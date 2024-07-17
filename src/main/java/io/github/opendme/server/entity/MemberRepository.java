package io.github.opendme.server.entity;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByDepartmentId(Long departmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.department = NULL WHERE m.department = :department")
    void removeDepartment(@Param("department") Department department);

    /**
     * Removes all department references.<br/>
     * <b>Just for testing!</b>
     */
    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.department = NULL")
    void removeAllDepartments();
}
