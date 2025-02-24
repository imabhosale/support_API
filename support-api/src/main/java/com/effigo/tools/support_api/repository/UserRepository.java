package com.effigo.tools.support_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effigo.tools.support_api.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); 
    User findByUserNameAndPassword(String userName, String password);
    boolean existsByEmail(String email);
}
