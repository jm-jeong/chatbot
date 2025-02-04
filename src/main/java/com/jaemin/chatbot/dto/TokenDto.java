package com.jaemin.chatbot.dto;

public record TokenDto(
	String accessToken,
	String refreshToken
) {
}
