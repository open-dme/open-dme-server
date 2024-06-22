package io.github.opendme.server.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {
    Flux<Member> findAllByDepartmentId(Long departmentId);
}
