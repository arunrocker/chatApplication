package com.arun.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		SecurityFilterChain filter = http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
		.oauth2Client(Customizer.withDefaults())
		.formLogin(form ->form.defaultSuccessUrl("/websocketChat")).csrf(csrf->csrf.disable()).build();
		return filter;
	}

}
