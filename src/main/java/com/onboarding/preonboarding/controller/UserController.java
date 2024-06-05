package com.onboarding.preonboarding.controller;


import com.onboarding.preonboarding.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@PostMapping("/signup")
	public ResponseEntity<UserDTO> registUser() {
        UserDTO  userDTO = null;
		ResponseEntity<UserDTO> ResponseEntity = null;
		return ResponseEntity;
	}
}
