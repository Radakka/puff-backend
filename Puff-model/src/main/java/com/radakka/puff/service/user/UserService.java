package com.radakka.puff.service.user;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.radakka.puff.authentication.user.Role;
import com.radakka.puff.dto.auth.RegisterRequest;
import com.radakka.puff.entity.user.User;
import com.radakka.puff.exception.UsernameAlreadyExistsException;
import com.radakka.puff.repository.user.UserRepository;
import com.radakka.puff.security.PBKDF2Encoder;
import com.radakka.puff.utils.EntityIdUtils;

import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PBKDF2Encoder passwordEncoder;
	
	public Mono<User> registerUser(RegisterRequest registerRequest) {
		User user = new User();
		user.setId(EntityIdUtils.getUserId(registerRequest.getUsername()));
		user.setUsername(registerRequest.getUsername());
		user.setRoles(Arrays.asList(Role.ROLE_USER));
		user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
		return this.userRepository.save(user).onErrorResume((exception) -> {
			if(exception instanceof OptimisticLockingFailureException) {
				return Mono.error(new UsernameAlreadyExistsException());
			} else {
				return Mono.error(exception);
			}
		});
	}
	
}
