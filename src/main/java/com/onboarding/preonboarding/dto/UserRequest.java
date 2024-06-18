package com.onboarding.preonboarding.dto;

import com.onboarding.preonboarding.entity.User;
import com.onboarding.preonboarding.utils.PasswordHasher;

import java.text.ParseException;
import java.util.Date;

public interface UserRequest {
	User convertToUserEntity(PasswordHasher passwordHasher) throws ParseException;

	Date birthDayToDate(String inputDate) throws ParseException;
}
