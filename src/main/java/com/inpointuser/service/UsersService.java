package com.inpointuser.service;

import com.inpointuser.domain.Users;
import com.inpointuser.repository.UsersRepository;
import com.inpointuser.service.dto.UsersDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional(readOnly = true)
    public Page<UsersDTO> getUserList(Pageable pageable) {
        return usersRepository.usersPage(pageable);
    }

    @Transactional
    public void addViewCount(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("not found with user id: " + userId));
        user.viewCountPlus();
    }
}
