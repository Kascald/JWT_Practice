package com.onboarding.preonboarding.SecureLogin;

import com.onboarding.preonboarding.dto.CustomUserDetials;
import com.onboarding.preonboarding.entity.RefreshToken;
import com.onboarding.preonboarding.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter   {
	private final AuthenticationManager authenticationManager;
	private final JWTTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public CustomLoginFilter(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenRepository = refreshTokenRepository;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		String username = obtainUsername(req);
		String password = obtainPassword(req);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		return authenticationManager.authenticate(authRequest);
	}

	@Override
	public void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		System.out.println("Successful Authentication");
		CustomUserDetials customUserDetials = (CustomUserDetials)auth.getPrincipal();
		String username = customUserDetials.getUsername();

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		List<String> roles = authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String accessToken = jwtTokenProvider.createAccessToken(username, roles);
		String refreshToken;

		Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findBySubject(username);
		if (existingRefreshToken.isPresent()) {
			String existingToken = existingRefreshToken.get().getRefreshToken();
			if (jwtTokenProvider.isRefreshTokenValid(existingToken)) {
				refreshToken = existingToken;
			} else {
				jwtTokenProvider.deleteRefreshToken(existingToken);
				refreshToken = jwtTokenProvider.createRefreshToken(username,roles);
			}
		} else {
			refreshToken = jwtTokenProvider.createRefreshToken(username,roles);
		}
		jwtTokenProvider.setAuthorizationHeaderForAccessToken(res, accessToken);
		jwtTokenProvider.setAuthorizationHeaderForRefreshToken(res, refreshToken);
	}

	@Override
	public void unsuccessfulAuthentication(HttpServletRequest req,
	                                       HttpServletResponse res,
	                                       AuthenticationException failed) throws IOException, ServletException{
		System.out.println("Unsuccessful Authentication");
	}
}
