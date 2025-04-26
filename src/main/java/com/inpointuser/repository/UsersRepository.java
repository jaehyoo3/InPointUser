package com.inpointuser.repository;

import com.inpointuser.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, UsersRepositoryCustom {
}
