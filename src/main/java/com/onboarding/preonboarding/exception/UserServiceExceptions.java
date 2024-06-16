package com.onboarding.preonboarding.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserServiceExceptions extends RuntimeException {
	UserServiceErrorCode errorCode;
}
