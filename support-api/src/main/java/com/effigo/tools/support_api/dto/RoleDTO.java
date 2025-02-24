package com.effigo.tools.support_api.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RoleDTO {
	  	private Long id;
	    private String roleName;
	    private Timestamp createdOn;
	    private String createdBy;

}
