package com.jaemin.chatbot.dto.response;

import com.jaemin.chatbot.dto.TokenDto;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
	public static TokenResponse fromDto(TokenDto tokenDto) {
		return new TokenResponse(tokenDto.accessToken(), tokenDto.refreshToken());
	}
}
