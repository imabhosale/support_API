package com.effigo.tools.support_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effigo.tools.support_api.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserNameAndPassword(String userName, String password);
}