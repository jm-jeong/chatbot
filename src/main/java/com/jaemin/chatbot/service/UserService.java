package com.jaemin.chatbot.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaemin.chatbot.dto.UserDto;
import com.jaemin.chatbot.entity.UserAccount;
import com.jaemin.chatbot.entity.constant.UserRole;
import com.jaemin.chatbot.repository.UserAccountRepository;

@Service
public class UserService {

	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

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
}
