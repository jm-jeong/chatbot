package com.jaemin.chatbot.dto;

import com.jaemin.chatbot.entity.UserAccount;

public record UserDto(
	Long id,
	String name,
	String email,
	String password,
	String gender,
	String phone) {

	public static UserDto of(String email, String password) {
		return of(null, email, password, null, null);
	}
	public static UserDto of(String name, String email,String password, String gender, String phone) {
		return new UserDto(null, name, email, password, gender, phone);
	}

	// 팩토리 메서드로 Entity -> DTO 변환
	public static UserDto fromEntity(UserAccount userAccount) {
		return new UserDto(
			userAccount.getId(),
			userAccount.getName(),
			userAccount.getEmail(),
			"",
			userAccount.getGender(),
			userAccount.getPhone()
		);
	}

}