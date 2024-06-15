package com.onboarding.preonboarding.controller;


import com.onboarding.preonboarding.dto.SignUpRequest;
import com.onboarding.preonboarding.dto.UserRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user/api")
public class UserController {

	@PostMapping("/signup")
	public ResponseEntity<UserRes> registUser(@RequestBody SignUpRequest signUpRequest) {
		// service .registration (userDto ) ;
		// response of service to ResponseEntity

		ResponseEntity<UserRes> ResponseEntity = null;
		return ResponseEntity;
	}


}
