package org.serverapp.domain.repository;

import org.serverapp.domain.entity.Member;
import org.serverapp.domain.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Page<Member> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Member> findByPosition(Position position);

    @EntityGraph(attributePaths = {"subordinates", "subordinates.position", "position"})
    Optional<Member> findById(UUID id);

}
