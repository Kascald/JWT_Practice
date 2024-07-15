package com.onboarding.preonboarding.config;


import com.onboarding.preonboarding.SecureLogin.CustomLoginFilter;
import com.onboarding.preonboarding.SecureLogin.JWTTokenProvider;
import com.onboarding.preonboarding.repository.RefreshTokenRepository;
import com.onboarding.preonboarding.utils.PasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	@Bean
	public PasswordHasher passwordHasher() {
		return new PasswordHasher(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((auth)-> auth.disable());
		http.formLogin((auth)-> auth.disable());
		http.httpBasic((auth)-> auth.disable());

		/* Lambda Expression
		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable); */

		http.authorizeHttpRequests((auth)-> auth
				.requestMatchers("/login", "/", "/join").permitAll()
				.anyRequest().authenticated());
		http.addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration),jwtTokenProvider,refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);

		http.sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}
}
