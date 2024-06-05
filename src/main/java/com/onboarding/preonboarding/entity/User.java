package com.onboarding.preonboarding.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

	@Id @Column(unique = true, nullable = false)
	private UUID id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

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

}
