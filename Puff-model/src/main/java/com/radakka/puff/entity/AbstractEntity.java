package com.radakka.puff.entity;

import org.springframework.data.annotation.Version;

import com.couchbase.client.java.repository.annotation.Id;

import lombok.Data;

@Data
public class AbstractEntity {
	
	@Id
	private String id;
	
	@Version
	private long version;

}
