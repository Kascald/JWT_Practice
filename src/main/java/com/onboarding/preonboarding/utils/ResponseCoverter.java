package com.onboarding.preonboarding.utils;

import com.onboarding.preonboarding.dto.UserRes;
import com.onboarding.preonboarding.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public class ResponseCoverter {
	UserRes userRes = null;
	public ResponseEntity<UserRes> zipSignUpResponse(User responsedUser,String responseMessage) throws Exception {
		try {
			userRes = ResponsePoolManager.borrowObject();

			if (responsedUser.isAccountExists()) {

				userRes.setStatus("201");
				userRes.setMessage("account create success");
				userRes.setRequestURI("user/api/signup");
				userRes.setUsername(responsedUser.getUsername());
				userRes.setFirstname(responsedUser.getFirstName());
				userRes.setLastname(responsedUser.getLastName());

				return ResponseEntity.status(HttpStatus.CREATED).body(userRes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (userRes != null) {
				ResponsePoolManager.returnObject(userRes);
			}
		}

		return ResponseEntity.status(HttpStatus.OK).body(userRes);
	}

	public ResponseEntity<UserRes> zipFindUserResponse(User foundUser) throws Exception {
		try {
			userRes = ResponsePoolManager.borrowObject();

			if (foundUser.isAccountExists()) {

				userRes.setStatus("200");
				userRes.setMessage("User found success");
				userRes.setRequestURI("user/api/find");
				userRes.setUsername(foundUser.getUsername());
				userRes.setFirstname(foundUser.getFirstName());
				userRes.setLastname(foundUser.getLastName());

				return ResponseEntity.status(HttpStatus.OK).body(userRes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (userRes != null) {
				ResponsePoolManager.returnObject(userRes);
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(userRes);
	}

}
