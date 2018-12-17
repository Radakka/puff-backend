package com.radakka.puff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

@Configuration
public class CouchbaseConfig {

	@Bean
	public Cluster couchbaseCluster(@Value("${com.puff.couchbase.host}") String host) {
		return CouchbaseCluster.create(host);
	}

	@Bean
	public Bucket couchbaseBucket(Cluster cluster, 
			@Value("${com.puff.couchbase.user}") String user,
			@Value("${com.puff.couchbase.password}") String password, 
			@Value("${com.puff.couchbase.bucket}") String bucket) {
		return cluster.authenticate(user, password).openBucket(bucket);
	}

}
