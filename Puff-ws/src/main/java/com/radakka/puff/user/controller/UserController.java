package com.radakka.puff.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.radakka.puff.dto.auth.AuthRequest;
import com.radakka.puff.dto.auth.AuthResponse;
import com.radakka.puff.dto.auth.RegisterRequest;
import com.radakka.puff.exception.UsernameAlreadyExistsException;
import com.radakka.puff.security.JWTUtils;
import com.radakka.puff.security.PBKDF2Encoder;
import com.radakka.puff.service.user.UserDetailsService;
import com.radakka.puff.service.user.UserService;

import reactor.core.publisher.Mono;

@RestController
public class UserController {
	
	@Autowired
	private JWTUtils jwtUtil;
	
	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest) {
		return this.userDetailsService.findByUsername(authRequest.getUsername()).map((userDetails) -> {
			if (passwordEncoder.encode(authRequest.getPassword()).equals(userDetails.getPassword())) {
				return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ResponseEntity<Object>> register(@RequestBody RegisterRequest registerRequest) {
		return this.userService.registerUser(registerRequest).map((savedUser) -> {
			return ResponseEntity.ok().build();
		}).onErrorResume((exception) -> {
			if(exception instanceof UsernameAlreadyExistsException) {
				return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists"));
			} else {
				return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
			}
		});
	}

}
