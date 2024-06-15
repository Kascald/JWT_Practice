package com.onboarding.preonboarding.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public class SignUpRequest {


	private String firstName;
	private String lastName;

	private String email;
	private String password;

	private String birthDay;
	private Date birthDayToDate;

	private String gender;
	private String phone;
	private String legion;

	public SignUpRequest(String firstName, String lastName, String email,
	               String password, String birthDay,
	               String gender, String phone, String legion) throws ParseException {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.birthDay = birthDay; // yyyy/MM/dd format

		this.gender = gender;
		this.phone = phone;
		this.legion = legion;

		SimpleDateFormat birthInput = new SimpleDateFormat("yyyy-MM-dd");
		this.birthDayToDate = birthInput.parse(birthDay);
	}
}
