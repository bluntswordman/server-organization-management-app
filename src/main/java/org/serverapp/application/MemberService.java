package org.serverapp.application;

import org.serverapp.application.dto.MemberDetailResponseDTO;
import org.serverapp.application.dto.MemberRequestDTO;
import org.serverapp.application.dto.MemberResponseDTO;
import org.serverapp.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {
    Page<MemberResponseDTO> getAllMembers(String search, int page, int size);

    Optional<MemberDetailResponseDTO> getMemberDetailById(UUID id);

    Member saveMember(MemberRequestDTO requestDTO) throws IOException;

}
