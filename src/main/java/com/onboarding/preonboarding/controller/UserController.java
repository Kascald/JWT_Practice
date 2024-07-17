package com.onboarding.preonboarding.controller;


import com.onboarding.preonboarding.dto.LoginRequest;
import com.onboarding.preonboarding.dto.SignUpRequest;
import com.onboarding.preonboarding.dto.UserRes;
import com.onboarding.preonboarding.service.AuthenticationService;
import com.onboarding.preonboarding.service.UserAuthenticationService;
import com.onboarding.preonboarding.service.UserRegistrationService;
import com.onboarding.preonboarding.utils.ResponseCoverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController
@RequestMapping(value = "/user/api")
public class UserController {
	private final AuthenticationService authenticationService;
	private final UserAuthenticationService userAuthenticationService;
	private final UserRegistrationService userRegistrationService;
	private final ResponseCoverter responseCoverter;

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController(AuthenticationService authenticationService,
	                      UserAuthenticationService userAuthenticationService, UserRegistrationService userRegistrationService, ResponseCoverter responseCoverter) {
		this.authenticationService = authenticationService;
		this.userAuthenticationService = userAuthenticationService;
		this.userRegistrationService = userRegistrationService;
		this.responseCoverter = responseCoverter;
	}

	@PostMapping("/signup")
	public ResponseEntity<UserRes> registUser(@RequestBody SignUpRequest signUpRequest) throws ParseException {
		ResponseEntity<UserRes> resResponseEntity;

		try {
			// service .registration (userDto ) ;
			userRegistrationService.userRegistration(signUpRequest);

			UserRes userRes = new UserRes("Ok","Done","/user/api/signup",
			                              signUpRequest.getEmail(),signUpRequest.getFirstName(),signUpRequest.getLastName());
			resResponseEntity= ResponseEntity.status(HttpStatus.OK).body(userRes);
			logger.info("응답 성공");
			return resResponseEntity;

		} catch (ParseException e) {
			// response of service to ResponseEntity
			UserRes userRes = new UserRes("Parse_error","Retry it","/user/api/signup",
			                              signUpRequest.getEmail(),signUpRequest.getFirstName(),signUpRequest.getLastName());
			resResponseEntity= ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userRes);
			return resResponseEntity;
		}

	}

//	@PostMapping("/login")
//	public ResponseEntity<UserRes> loginUser(@RequestBody LoginRequest loginRequest) {
//		ResponseEntity<UserRes> resResponseEntity;
//		try {
//			// service .registration (userDto ) ;
//			userRegistrationService.userRegistration(loginRequest);
//
//			UserRes userRes = new UserRes("Ok","Done","/user/api/signup",
//			                              loginRequest.getEmail(),loginRequest.getFirstName(),loginRequest.getLastName());
//			resResponseEntity= ResponseEntity.status(HttpStatus.OK).body(userRes);
//			logger.info("응답 성공");
//			return resResponseEntity;
//
//		} catch (ParseException e) {
//			// response of service to ResponseEntity
//			UserRes userRes = new UserRes("Parse_error","Retry it","/user/api/signup",
//			                              loginRequest.getEmail(),loginRequest.getFirstName(),loginRequest.getLastName());
//			resResponseEntity= ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userRes);
//			return resResponseEntity;
//		}
//	}


}
