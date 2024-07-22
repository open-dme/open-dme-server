package io.github.opendme.server.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CallResponseRepository extends JpaRepository<CallResponse, Long> {
    List<CallResponse> findAllByMember_Department_IdAndCreatedAtAfterAndCallNull(Long id, LocalDateTime createdAt);
}
