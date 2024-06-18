package com.onboarding.preonboarding.service;

import com.onboarding.preonboarding.dto.SignUpRequest;
import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.exception.UserServiceExceptions;
import com.onboarding.preonboarding.repository.UserRepository;
import com.onboarding.preonboarding.utils.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.onboarding.preonboarding.exception.UserServiceErrorCode.ALREADY_EXISTS_USERNAME;

@Service
public class UserRegistrationService implements UserServiceGeneral {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final PasswordHasher passwordHasher;
	private final UserRepository userRepository;
	private final UserFindService userFindService;

	public UserRegistrationService(PasswordHasher passwordEncoder, UserRepository userRepository, UserFindService userFindService) {
		this.passwordHasher = passwordEncoder;
		this.userRepository = userRepository;
		this.userFindService = userFindService;
	}


	public void userRegistration(SignUpRequest signUpRequest) throws Exception {
		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);
		inputUser.setPassword(passwordHasher.hash(signUpRequest.getPassword()));

		Optional<User> foundUser = Optional.ofNullable(userFindService.findByUsername(inputUser.getUsername()));
		loggingObject(foundUser,logger);

		if(foundUser.isPresent()) {
			throw new UserServiceExceptions(ALREADY_EXISTS_USERNAME);
		} else {
			userRepository.save(inputUser);
		}
	}



}
