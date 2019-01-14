package com.radakka.puff.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager, ServerSecurityContextRepository securityContextRepository) {
		http
		.addFilterAt(this.corsFilter(), SecurityWebFiltersOrder.FIRST)
		.csrf().disable()
		.httpBasic().disable()
		.logout().disable()
		.authenticationManager(authenticationManager)
		.securityContextRepository(securityContextRepository)
		.authorizeExchange()
		.pathMatchers(HttpMethod.OPTIONS).permitAll()
		.pathMatchers("/login").permitAll()
		.anyExchange().authenticated();

		return http.build();
	}
	
	public WebFilter corsFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.applyPermitDefaultValues();
	 
	    UrlBasedCorsConfigurationSource source =
	      new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfig);
	 
	    return new CorsWebFilter(source);
	}

}
