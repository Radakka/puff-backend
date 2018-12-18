package com.radakka.puff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.radakka.puff.security.AuthenticationManager;
import com.radakka.puff.security.SecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
		http
		.csrf().disable()
		.httpBasic().disable()
		.authenticationManager(authenticationManager)
		.securityContextRepository(securityContextRepository)
		.authorizeExchange()
		.pathMatchers(HttpMethod.OPTIONS).permitAll()
		.pathMatchers("/login").permitAll()
		.anyExchange().authenticated();

		return http.build();
	}

}
