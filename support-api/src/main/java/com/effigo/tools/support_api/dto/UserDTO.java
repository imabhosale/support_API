package com.effigo.tools.support_api.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class UserDTO {

	private Long id;
	private String userName;
	private String password;
	private String email;
	private Instant createdOn = Instant.now();
	private String createdBy;
}
