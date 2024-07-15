package com.onboarding.preonboarding.SecureLogin;

import com.onboarding.preonboarding.entity.RefreshToken;
import com.onboarding.preonboarding.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JWTTokenProviderTest {

	@MockBean
	private RefreshTokenRepository refreshTokenRepository;

	private JWTTokenProvider jwtTokenProvider;

	@Value("${myJwtRandomKeyHashed}")
	private String jwtSecretKey;

	@Value("${accessTokenExpiration}")
	private long accessExpiration;

	@Value("${refreshTokenExpiration}")
	private long refreshExpiration;

	@BeforeEach
	public void setUp() {
		jwtTokenProvider = new JWTTokenProvider(refreshTokenRepository, jwtSecretKey, accessExpiration, refreshExpiration);
	}

	@Test
	public void testGenerateToken() {
		String token = jwtTokenProvider.generateToken("testUser", Arrays.asList("ROLE_USER"), accessExpiration);
		assertNotNull(token);
		assertTrue(jwtTokenProvider.validateToken(token));
	}

	@Test
	public void testGetUsernameFromToken() {
		String token = jwtTokenProvider.generateToken("testUser",  Arrays.asList("ROLE_USER"), accessExpiration);
		String username = jwtTokenProvider.getUsernameFromToken(token);
		assertEquals("Test User", username);
	}

	@Test
	public void testGetRoleListFromToken() {
		String token = jwtTokenProvider.generateToken("testUser",  Arrays.asList("ROLE_USER"), accessExpiration);
		Claims claims = jwtTokenProvider.parsePayloadFromToken(token);
		List<String> roles = claims.get("userRole", List.class);
		assertEquals(1, roles.size());
		assertEquals("ROLE_USER", roles.get(0));
	}

	@Test
	public void testCreateAccessToken() {
		String token = jwtTokenProvider.createAccessToken("testUser", Arrays.asList("ROLE_USER"));
		assertNotNull(token);
		assertTrue(jwtTokenProvider.validateToken(token));
	}

	@Test
	public void testCreateRefreshToken() {
		String token = jwtTokenProvider.createRefreshToken("testUser", Arrays.asList("ROLE_USER"));
		assertNotNull(token);
		assertTrue(jwtTokenProvider.validateToken(token));
	}

	@Test
	public void testSaveRefreshToken() {
		String refreshToken = jwtTokenProvider.createRefreshToken("testUser",  Arrays.asList("ROLE_USER"));
		jwtTokenProvider.saveRefreshToken(refreshToken);
		verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
	}

	@Test
	public void testValidateToken() {
		String token = jwtTokenProvider.createAccessToken("testUser",  Arrays.asList("ROLE_USER"));
		assertTrue(jwtTokenProvider.validateToken(token));
	}

	@Test
	public void testIsTokenNotExpired() {
		String token = jwtTokenProvider.createAccessToken("testUser",  Arrays.asList("ROLE_USER"));
		assertFalse(jwtTokenProvider.isTokenNotExpiration(token));
	}

	@Test
	public void testIsExistsRefreshToken() {
		String refreshToken = jwtTokenProvider.createRefreshToken("testUser", Arrays.asList("ROLE_USER"));
		jwtTokenProvider.saveRefreshToken(refreshToken);
		assertFalse(jwtTokenProvider.isExistsRefreshToken(refreshToken));
	}
}
