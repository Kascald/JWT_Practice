package com.onboarding.preonboarding.service;

import com.onboarding.preonboarding.dto.SignUpRequest;
import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.exception.UserServiceExceptions;
import com.onboarding.preonboarding.repository.UserRepository;
import com.onboarding.preonboarding.utils.ResponseCoverter;
import jakarta.transaction.TransactionRolledbackException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

import static com.onboarding.preonboarding.exception.UserServiceErrorCode.ALREADY_EXISTS_USERNAME;
import static com.onboarding.preonboarding.exception.UserServiceErrorCode.USER_NOT_FOUND;

@Service
public class UserService {//implements UserDetailsService {
    private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder ) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		return ;
//	}

	public void userRegistration(SignUpRequest signUpRequest) throws Exception {
		User user = signUpRequest.convertToUserEntity(passwordEncoder);
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

		if(!findByUsername(user.getUsername())) {
			userRepository.save(user);
		} else {
			throw new UserServiceExceptions(ALREADY_EXISTS_USERNAME);
		}
	}

	private Boolean findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(
				()-> new UserServiceExceptions(USER_NOT_FOUND)).isAccountExists();
	}
}
