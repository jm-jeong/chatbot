package com.jaemin.chatbot.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jaemin.chatbot.config.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Value("${frontend.server-url}")
	private String frontendServerUrl;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)// CSRF 비활성화 (REST API의 경우)
			.sessionManagement(httpSecuritySessionManagementConfigurer -> {
				httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			})
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll() // Swagger는 인증 없이 허용
				.requestMatchers("/api/v1/users/**").permitAll()// 그 외 요청은 인증 필요
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		// CorsConfiguration configuration = new CorsConfiguration();
		// configuration.addAllowedOrigin("*");
		// configuration.addAllowedMethod("*");
		// configuration.addAllowedHeader("*");
		// configuration.setAllowedOrigins(Arrays.asList(frontendServerUrl, "*"));
		// configuration.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT"));
		// configuration.setAllowedHeaders(Collections.singletonList("*"));
		// configuration.setAllowCredentials(true);
		// UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// source.registerCorsConfiguration("/**", configuration);

		CorsConfiguration configuration = new CorsConfiguration();
		// configuration.setAllowCredentials(true);//TODO:토큰때문에 기능 켜야한다고 생각했는데, CORS해제 안되서 끔
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.addExposedHeader("*");
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(
			Arrays.asList(frontendServerUrl, "http://localhost:3000", "https://127.0.0.1:3000",
				"http://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
