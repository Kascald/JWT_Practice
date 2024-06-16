package com.onboarding.preonboarding.service;

import com.onboarding.preonboarding.dto.SignUpRequest;
import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.repository.UserRepository;
import com.onboarding.preonboarding.utils.ResponseCoverter;
import jakarta.transaction.TransactionRolledbackException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

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

	public String userRegistration(SignUpRequest signUpRequest) throws Exception {
		User user = signUpRequest.convertToUserEntity();
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		userRepository.save(user);
		if(findByUsername(user.getUsername())!=null) {
	        return "success";
		}else {
			return "fail";
		}
	}

	private User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User findByUsername2(String username) {
		return userRepository.findByUsername(username);
	}
}
