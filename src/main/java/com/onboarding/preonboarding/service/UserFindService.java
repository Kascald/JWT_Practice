package com.onboarding.preonboarding.service;

import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.exception.UserServiceExceptions;
import com.onboarding.preonboarding.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.onboarding.preonboarding.exception.UserServiceErrorCode.USER_NOT_FOUND;

@Service
public class UserFindService implements UserServiceGeneral{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final UserRepository userRepository;

	public UserFindService(UserRepository userRepository) {this.userRepository = userRepository;}

	//유저 이름기반 조회
	public  User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(
				()-> new UserServiceExceptions(USER_NOT_FOUND));
	}
//	public User findByUsername(String firstName, String LastName)

	//유저 패스워드 매칭확인
	//유저 정밀 조회
	//유저정보 기반 필터링 정보제공 - legion
	//유저정보 기반 필터링 정보 제공 - gender
	public boolean compareUserInfo(User inputUserInfo, User foundeUserInfo) {
		loggingObject(inputUserInfo,logger);
		loggingObject(foundeUserInfo,logger);

		String foundName = foundeUserInfo.getFirstName() + foundeUserInfo.getLastName();
		String inputName = inputUserInfo.getFirstName() + inputUserInfo.getLastName();
		int compareScore = 0;

		if (inputUserInfo.getId() != null && inputUserInfo.getId().equals(foundeUserInfo.getId()))
			compareScore++;

		if (inputName.equals(foundName) )
			compareScore++;

		if (inputUserInfo.getPhone() != null && inputUserInfo.getPhone().equals(foundeUserInfo.getPhone()))
			compareScore++;

		if (inputUserInfo.getEmail() != null && inputUserInfo.getEmail().equals(foundeUserInfo.getEmail()))
			compareScore++;

		return compareScore > 0;
	}

}
