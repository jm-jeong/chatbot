package com.jaemin.chatbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaemin.chatbot.config.auth.UserAccountPrincipal;
import com.jaemin.chatbot.dto.request.TokenRequest;
import com.jaemin.chatbot.dto.request.UserLoginRequest;
import com.jaemin.chatbot.dto.request.UserSignupRequest;
import com.jaemin.chatbot.dto.response.TokenResponse;
import com.jaemin.chatbot.dto.response.UserInfoResponse;
import com.jaemin.chatbot.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User API", description = "회원가입 및 사용자 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원가입", description = "이메일, 이름, 비밀번호, 성별, 휴대폰번호를 통해 회원가입을 진행합니다.")
	@PostMapping
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserSignupRequest userSignupRequest) {
		userService.registerUser(userSignupRequest.toDto());
		return ResponseEntity.ok("User registered successfully");
	}

	@Operation(summary = "사용자 조회", description = "사용자 ID를 통해 정보를 조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<UserInfoResponse> getUser(@PathVariable Long id) {
		UserInfoResponse userInfoResponse = UserInfoResponse.fromDto(userService.getUserById(id));
		return ResponseEntity.ok(userInfoResponse);
	}

	@Operation(summary = "사용자 로그인", description = "email, password 통해 로그인합니다.")
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
		TokenResponse tokenResponse = TokenResponse.fromDto(userService.login(userLoginRequest.toDto()));
		return ResponseEntity.ok(tokenResponse);
	}

	@Operation(summary = "refresh token", description = "refresh token 으로 access token 재발급합니다.")
	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody TokenRequest tokenRequest) {
		TokenResponse tokenResponse = TokenResponse.fromDto(userService.refreshToken(tokenRequest.refreshToken()));
		return ResponseEntity.ok(tokenResponse);

	}

	@GetMapping("/myInfo")
	public ResponseEntity<?> myInfo(Authentication authentication){
		UserAccountPrincipal userAccountPrincipal = (UserAccountPrincipal) authentication.getPrincipal();
		UserInfoResponse userInfoResponse = UserInfoResponse.fromDto(userService.myInfo(userAccountPrincipal.getUsername()));
		return ResponseEntity.ok(userInfoResponse);
		// return new ResponseEntity<>(new ResponseDto<>(ResponseEnum.USER_MY_INFO_SUCCESS, result), HttpStatus.OK);

	}


	// @GetMapping("/myInfo")
	// public ResponseEntity<?> myInfo(Authentication authentication){
	// 	UserAccountPrincipal userAccountPrincipal = (UserAccountPrincipal) authentication.getPrincipal();
	// 	UserDto result = userService.myInfo(details.getUser().getIdx());
	// 	return new ResponseEntity<>(new ResponseDto<>(ResponseEnum.USER_MY_INFO_SUCCESS, result), HttpStatus.OK);
	// }
	//
	// @GetMapping("/nicknameCk")
	// public ResponseEntity<?> nicknameCk(@RequestParam(value="nickname") String nickname){
	// 	userService.nicknameCk(nickname);
	// 	return new ResponseEntity<>(new ResponseDto<>(ResponseEnum.USER_USERNAME_CK_SUCCESS), HttpStatus.OK);
	// }
}
