package com.effigo.tools.support_api.model;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_detail")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "created_on", nullable = false)
    private Instant createdOn = Instant.now();

    @Column(name = "created_by", nullable = false)
    private String createdBy;

 
}