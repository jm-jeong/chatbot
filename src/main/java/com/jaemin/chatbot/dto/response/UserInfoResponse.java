package com.jaemin.chatbot.dto.response;

import com.jaemin.chatbot.dto.UserDto;

public record UserInfoResponse(
	Long id,
	String email,
	String name,
	String gender,
	String phone
) {

	public static UserInfoResponse fromDto(UserDto userDto) {
		return new UserInfoResponse(
			userDto.id(),
			userDto.email(),
			userDto.name(),
			userDto.gender(),
			userDto.phone()
		);
	}

}
