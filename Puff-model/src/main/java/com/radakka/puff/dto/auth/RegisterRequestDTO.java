package com.radakka.puff.dto.auth;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterRequestDTO {

	@NotBlank(message="Username is required")
	private String username;
	
	@NotBlank(message="Password is required")
	private String password;
	
}
