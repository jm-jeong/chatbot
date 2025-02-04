package com.jaemin.chatbot.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaemin.chatbot.config.jwt.JwtUtil;
import com.jaemin.chatbot.dto.TokenDto;
import com.jaemin.chatbot.dto.UserDto;
import com.jaemin.chatbot.entity.UserAccount;
import com.jaemin.chatbot.entity.constant.UserRole;
import com.jaemin.chatbot.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public void registerUser(UserDto userDto) {
		if (userAccountRepository.findByEmail(userDto.email()).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}

		if (userAccountRepository.findByPhone(userDto.phone()).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 휴대폰 번호입니다.");
		}

		UserAccount userAccount = UserAccount.builder()
			.name(userDto.name())
			.email(userDto.email())
			.password(passwordEncoder.encode(userDto.password()))
			.gender(userDto.gender())
			.phone(userDto.phone())
			.userRole(UserRole.ROLE_USER)
			.build();

		userAccountRepository.save(userAccount);
	}

	@Transactional(readOnly = true)
	public UserDto getUserById(Long id) {
		UserAccount userAccount = userAccountRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

		return UserDto.fromEntity(userAccount);
	}

	@Transactional(readOnly = true)
	public TokenDto login(UserDto userDto) {
		Optional<UserAccount> foundUserAccount = userAccountRepository.findByEmail(userDto.email());
		if (foundUserAccount.isPresent() && passwordEncoder.matches(userDto.password(), foundUserAccount.get().getPassword())) {
			String accessToken = jwtUtil.generateToken(userDto.email(), false);
			String refreshToken = jwtUtil.generateToken(userDto.email(), true);
			return new TokenDto(accessToken, refreshToken);
		}
		return null; //TODO : 로그인 실패시 exception 처리 ResponseEntity.status(401).body("Invalid credentials");
	}

	public TokenDto refreshToken(String token) {
		if (jwtUtil.validateToken(token)) {
			String email = jwtUtil.getUsername(token);
			String newAccessToken = jwtUtil.generateToken(email, false);
			return new TokenDto(newAccessToken, token);
		}
		return null; //TODO : 토큰 리프레쉬 실패 시 ResponseEntity.status(403).body("Invalid refresh token");
	}

	public UserDto myInfo(String email){
		UserAccount userAccount = userAccountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));

		// User user = userAccountRepository.findByEmail(email).orElseThrow(() -> new CustomApiException(ResponseEnum.USER_NOT_FOUND));
		return UserDto.fromEntity(userAccount);
	}
}
