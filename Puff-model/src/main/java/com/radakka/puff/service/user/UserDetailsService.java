package com.radakka.puff.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.radakka.puff.entity.user.User;
import com.radakka.puff.repository.user.UserRepository;
import com.radakka.puff.utils.UserIdUtils;

import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return this.userRepository.findById(UserIdUtils.getUserId(username)).switchIfEmpty(Mono.defer(() -> {
			return Mono.error(new UsernameNotFoundException("User Not Found"));
		})).map(User::toUserDetails);
	}
	
	
}
