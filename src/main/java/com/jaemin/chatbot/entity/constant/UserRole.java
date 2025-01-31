package com.jaemin.chatbot.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
	ROLE_USER("사용자"),
	ROLE_ADMIN("관리자");

	private final String description;
}
