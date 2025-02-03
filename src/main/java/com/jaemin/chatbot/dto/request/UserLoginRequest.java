package com.jaemin.chatbot.dto.request;

import com.jaemin.chatbot.dto.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 주소를 입력하세요.")
	String email,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	String password
) {

	public UserDto toDto() {
		return UserDto.of(email, password);
	}
}
