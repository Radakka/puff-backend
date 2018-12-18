package com.radakka.puff.entity.user;

import javax.validation.constraints.NotNull;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

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
}
