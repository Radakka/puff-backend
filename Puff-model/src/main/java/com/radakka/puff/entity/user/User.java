package com.radakka.puff.entity.user;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.radakka.puff.authentication.user.PuffUserDetails;
import com.radakka.puff.authentication.user.Role;

import lombok.Data;

@Document
@Data
public class User {
	@Id
	private String id;
	
	@Field
	@NotNull
	private String username;
	
	@Field
	@NotNull
	private String password;
	
	public PuffUserDetails toUserDetails() {
		PuffUserDetails userDetails = new PuffUserDetails();
		userDetails.setUsername(this.username);
		userDetails.setPassword(this.password);
		userDetails.setRoles(Arrays.asList(Role.ROLE_USER));
		userDetails.setEnabled(true);
		return userDetails;
	}
}
