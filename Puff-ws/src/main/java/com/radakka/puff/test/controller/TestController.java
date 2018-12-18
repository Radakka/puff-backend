package com.radakka.puff.test.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class TestController {
	
	@RequestMapping("/test")
	@PreAuthorize("hasRole('USER')")
	public Mono<String> test(@AuthenticationPrincipal String username) {
		return Mono.just("Test successful for "+username);
	}

}
