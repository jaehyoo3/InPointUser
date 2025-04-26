package com.inpointuser.repository;

import com.inpointuser.service.dto.UsersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersRepositoryCustom {
    Page<UsersDTO> usersPage(Pageable pageable);
}
