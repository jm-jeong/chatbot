package com.jaemin.chatbot.config.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	//TODO : SECRET_KEY, TOKEN_EXPIRATION application.yaml 로 빼고 암호화할 것
	private static final String SECRET_KEY = "secret_key_chatbot_asdf_zxcv_qwer";
	private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30분
	private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 1일

	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

	public String generateToken(String username, boolean isRefresh) {
		long expiration = isRefresh ? REFRESH_TOKEN_EXPIRATION : ACCESS_TOKEN_EXPIRATION;
		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String getUsername(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key).build()
			.parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
