package com.radakka.puff.repository.user;

import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

import com.radakka.puff.entity.user.User;

public interface UserRepository extends ReactiveCouchbaseRepository<User, String> {

}
