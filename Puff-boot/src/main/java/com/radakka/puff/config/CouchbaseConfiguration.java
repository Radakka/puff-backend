package com.radakka.puff.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractReactiveCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

@Configuration
@EnableReactiveCouchbaseRepositories(basePackages= {"com.radakka.puff.repository"})
public class CouchbaseConfiguration extends AbstractReactiveCouchbaseConfiguration {
	
	@Value("${com.puff.couchbase.host}")
	private String host;
	
	@Value("${com.puff.couchbase.bucket}")
	private String bucket;
	
	@Value("${com.puff.couchbase.password}")
	private String password;

	@Override
	protected List<String> getBootstrapHosts() {
		return Arrays.asList(this.host);
	}

	@Override
	protected String getBucketName() {
		return this.bucket;
	}

	@Override
	protected String getBucketPassword() {
		return this.password;
	}

}
