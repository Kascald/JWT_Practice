package com.onboarding.preonboarding.dto;

import com.onboarding.preonboarding.entity.User;
import jakarta.validation.groups.Default;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface UserRequest {
	User convertToUserEntity(PasswordEncoder passwordEncoder) throws ParseException;

	Date birthDayToDate(String inputDate) throws ParseException;
}
