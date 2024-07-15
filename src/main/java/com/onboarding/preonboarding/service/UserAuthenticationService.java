package com.onboarding.preonboarding.service;

import com.onboarding.preonboarding.dto.CustomUserDetials;
import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {

	private final UserFindService userFindService;

	public UserAuthenticationService(UserFindService userFindService) {this.userFindService = userFindService;}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userFindService.findByUsername(username);

		if (user == null) {
			return new CustomUserDetials(user);
		}
		return null;
	}
}
