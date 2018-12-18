package com.radakka.puff.entity.user;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.radakka.puff.authentication.user.PuffUserDetails;
import com.radakka.puff.authentication.user.Role;
import com.radakka.puff.entity.AbstractEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document
@Data
@EqualsAndHashCode(callSuper=true)	
public class User extends AbstractEntity {

	@Field
	@NotNull
	private String username;

	@Field
	@NotNull
	private String password;

	@Field
	private List<Role> roles;

	public PuffUserDetails toUserDetails() {
		PuffUserDetails userDetails = new PuffUserDetails();
		userDetails.setUsername(this.username);
		userDetails.setPassword(this.password);
		userDetails.setRoles(new ArrayList<>(this.roles));
		userDetails.setEnabled(true);
		return userDetails;
	}
}
