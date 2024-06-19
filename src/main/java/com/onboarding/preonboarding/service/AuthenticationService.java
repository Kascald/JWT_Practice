package com.onboarding.preonboarding.service;

import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.exception.UserServiceExceptions;
import com.onboarding.preonboarding.utils.PasswordHasher;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserFindService userFindService;
	private final PasswordHasher passwordHasher;

	public AuthenticationService(UserFindService userFindService, PasswordHasher passwordHasher) {
		this.userFindService = userFindService;
		this.passwordHasher = passwordHasher;
	}

	//유저 패스워드 매칭확인
	public boolean isMatchPassword(String username, String rawPassword) {
		User foundUser = null;
		try {
			foundUser =  userFindService.findByUsername(username);
			return passwordHasher.match(rawPassword ,foundUser.getPassword());
		} catch (UserServiceExceptions e) {
			return false;
		}
	}
}
