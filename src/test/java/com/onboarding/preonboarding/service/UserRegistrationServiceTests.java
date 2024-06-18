package com.onboarding.preonboarding.service;

import com.onboarding.preonboarding.dto.SignUpRequest;
import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.repository.UserRepository;
import com.onboarding.preonboarding.utils.PasswordHasher;
import org.junit.jupiter.api.Test;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;


@SpringBootTest
@WithMockUser(username = "user" , password = "1234")
public class UserRegistrationServiceTests {

	@Autowired
	private UserService userService;

//	@Autowired
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	private UserFindService userFindService;

	@Autowired
	private PasswordHasher passwordHasher;

	public User createUserUser() {
		return User.builder()
				.username("test@test.com")
				.password("1234")
				.firstName("kim")
				.lastName("geunHwi")
				.email("test@test.com")
				.gender("Male")
				.phone("010-0000-0000")
				.legion("South Korea")
				.build();
	}

	@Test
	public void 유저가입테스트() throws Exception {
		//give
		SignUpRequest signUpRequest =
				new SignUpRequest("Kim" ,"geunHwi","test@test.com"
						,"1234","2024/06/16","Male","010-0000-0000"
						,"South Korea");

		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);

		when(userRepository.findByUsername("test@test.com")).thenReturn(Optional.ofNullable(inputUser));


		//when
		userService.userRegistration(signUpRequest);
		User foundUser = (userFindService.findByUsername("test@test.com"));

		//then
		boolean isEqual = userFindService.compareUserInfo(inputUser, foundUser);
		userFindService.loggingObject(inputUser, logger);
		userFindService.loggingObject(foundUser, logger);


		assertTrue("User objects are not equal", isEqual);
	}
}