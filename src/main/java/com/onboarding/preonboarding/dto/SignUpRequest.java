package com.onboarding.preonboarding.dto;

import com.onboarding.preonboarding.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public class SignUpRequest implements UserRequest{


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
	}

	@Override
	public User convertToUserEntity(PasswordEncoder passwordEncoder) throws ParseException {
		Date thisUserBirthDayDate = this.birthDayToDate(this.getBirthDay());
		return User.builder()
				.username(this.getEmail())
				.password(passwordEncoder.encode(this.getPassword()))
				.firstName(this.getFirstName())
				.lastName(this.getLastName())
				.email(this.getEmail())
				.gender(this.getGender())
				.birthDay(thisUserBirthDayDate)
				.phone(this.getPhone())
				.legion(this.legion)
				.build();
	}

	@Override
	public Date birthDayToDate(String inputDate) throws ParseException {
		SimpleDateFormat birthInput = new SimpleDateFormat("yyyy/MM/dd");
		Date parsedDate = birthInput.parse(inputDate);

		SimpleDateFormat birthOutput = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = birthOutput.format(parsedDate);

		return birthOutput.parse(formattedDate);
	}
}
