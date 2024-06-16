package com.onboarding.preonboarding.entity;


import com.onboarding.preonboarding.dto.SignUpRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

	@Id @Column(unique = true, nullable = false)
	private UUID id;

	private String username;
	private String password;

	private String firstName;
	private String lastName;

	private String email;
	private String gender;
	private Date birthDay;

	private String phone;
	private String legion;

	@ElementCollection
	private List<String> roleList;

	// 생성자 또는 @PrePersist 메소드에서 id를 초기화
	@PrePersist
	public void initializeUUID() {
		if (id == null) {
			id = UUID.randomUUID();
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		this.roleList.stream().map( role-> authorities.add(new SimpleGrantedAuthority(role)));
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public User extractionFromSignUpRequest(SignUpRequest signUpRequest) {
		return User.builder()
				//.id() Auto Creation
				.username(signUpRequest.getEmail())
				//password() Setter로 추가할 것
				.firstName(signUpRequest.getFirstName())
				.lastName(signUpRequest.getLastName())
				.email(signUpRequest.getEmail())
				.gender(signUpRequest.getGender())
				.phone(signUpRequest.getPhone())
				.legion(signUpRequest.getLegion())
				.build();
	}

	public boolean isAccountExists() {
		if (this.username == null)
		    return false;
		else
			return true;
	}


}
