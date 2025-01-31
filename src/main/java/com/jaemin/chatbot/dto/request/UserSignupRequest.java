package com.jaemin.chatbot.dto.request;

import com.jaemin.chatbot.dto.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignupRequest(
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	String name,
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 주소를 입력하세요.")
	String email,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	String password,
	@NotBlank(message = "성별은 필수 입력 항목입니다.")
	@Pattern(regexp = "^(male|female)$", message = "성별은 'male' 또는 'female'이어야 합니다.")
	String gender,
	@NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
	@Pattern(regexp = "^\\d{10,11}$", message = "휴대폰 번호는 10~11자리 숫자여야 합니다.")
	String phone
) {
	public UserDto toDto() {
		return UserDto.of(name, email, password, gender, phone);
	}

}
