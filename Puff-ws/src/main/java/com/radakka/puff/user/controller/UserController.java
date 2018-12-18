package com.radakka.puff.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radakka.puff.entity.user.User;
import com.radakka.puff.repository.user.UserRepository;
import com.radakka.puff.utils.UserIdUtils;

import reactor.core.publisher.Mono;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/test")
	public Mono<User> test() {
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("test");
		user.setId(UserIdUtils.getUserId("testUser"));
		
		return this.userRepository.save(user);
		
		//return this.userRepository.findById(UserIdUtils.getUserId("testUser"));
	}

}
