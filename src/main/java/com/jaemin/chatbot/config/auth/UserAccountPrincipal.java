package com.jaemin.chatbot.config.auth;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jaemin.chatbot.entity.UserAccount;
import com.jaemin.chatbot.entity.constant.UserRole;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserAccountPrincipal implements UserDetails {
	private Long id;
	private String email;
	private String password;
	private UserRole userRole;

	public static UserAccountPrincipal fromEntity(UserAccount userAccount) {
		return new UserAccountPrincipal(userAccount.getId(), userAccount.getEmail(), userAccount.getPassword(), userAccount.getUserRole());
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(userRole::name);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
