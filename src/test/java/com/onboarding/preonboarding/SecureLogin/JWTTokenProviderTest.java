import com.onboarding.preonboarding.SecureLogin.JWTTokenProvider;
import com.onboarding.preonboarding.entity.RefreshToken;
import com.onboarding.preonboarding.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JWTTokenProviderTest {

	@InjectMocks
	private JWTTokenProvider jwtTokenProvider;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Mock
	private HttpServletResponse response;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		assertNotNull(jwtTokenProvider);
	}

	@Test
	public void 액세스_토큰_생성_테스트() {
		String username = "testuser";
		String userRealName = "Test User";
		List<String> userRoles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

		String accessToken = jwtTokenProvider.createAccessToken(username, userRealName, userRoles);

		assertNotNull(accessToken);
		assertTrue(jwtTokenProvider.validateToken(accessToken));
	}

	@Test
	public void 리프레시_토큰_생성_테스트() {
		String username = "testUser";
		String userRealName = "Test User";
		List<String> userRole = List.of("ROLE_USER");

		when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(new RefreshToken());

		String refreshToken = jwtTokenProvider.createRefreshToken(username, userRealName, userRole);

		Assert.notNull(refreshToken, "Refresh token should not be null");
		verify(refreshTokenRepository, atLeastOnce()).save(any(RefreshToken.class));
	}

	@Test
	public void 토큰_유효성_검사_테스트() {
		String validToken = "valid_token_string";
		String invalidToken = "invalid_token_string";

		// Mock parsePayloadFromToken 메서드를 설정하여 유효 기간이 지난 토큰을 만듦
		Claims claims = createClaimsWithExpiration(false);
		when(jwtTokenProvider.parsePayloadFromToken(validToken)).thenReturn(claims);
		when(jwtTokenProvider.parsePayloadFromToken(invalidToken)).thenThrow(new RuntimeException());

		assertTrue(jwtTokenProvider.validateToken(validToken));
		assertFalse(jwtTokenProvider.validateToken(invalidToken));
	}

	@Test
	public void 토큰_만료_검사_테스트() {
		String validToken = "valid_token_string";
		String expiredToken = "expired_token_string";

		Claims validClaims = createClaimsWithExpiration(false);
		Claims expiredClaims = createClaimsWithExpiration(true);

		when(jwtTokenProvider.parsePayloadFromToken(validToken)).thenReturn(validClaims);
		when(jwtTokenProvider.parsePayloadFromToken(expiredToken)).thenReturn(expiredClaims);

		assertTrue(jwtTokenProvider.isTokenNotExpiration(validToken));
		assertFalse(jwtTokenProvider.isTokenNotExpiration(expiredToken));
	}

	private Claims createClaimsWithExpiration(boolean isExpired) {
		Claims claims = mock(Claims.class);
		long currentTimeMillis = System.currentTimeMillis();

		if (isExpired) {
			when(claims.getExpiration()).thenReturn(new Date(currentTimeMillis - 1000)); // 만료된 토큰
		} else {
			when(claims.getExpiration()).thenReturn(new Date(currentTimeMillis + 1000)); // 유효한 토큰
		}

		return claims;
	}

	@Test
	public void 액세스_토큰_헤더_설정_테스트() {
		String accessToken = "valid_access_token";

		jwtTokenProvider.setAuthorizationHeaderForAccessToken(response, accessToken);

		verify(response).setHeader("authorization", "bearer " + accessToken);
	}

	@Test
	public void 리프레시_토큰_헤더_설정_테스트() {
		String refreshToken = "valid_refresh_token";

		jwtTokenProvider.setAuthorizationHeaderForRefreshToken(response, refreshToken);

		verify(response).setHeader("authorization", "bearer " + refreshToken);
	}

	@Test
	public void 리프레시_토큰_존재_여부_테스트() {
		String refreshTokenValue = "valid_refresh_token";
		RefreshToken refreshTokenEntity = new RefreshToken();
		refreshTokenEntity.setRefreshToken(refreshTokenValue);

		when(refreshTokenRepository.findByRefreshToken(refreshTokenValue)).thenReturn(Optional.of(refreshTokenEntity));

		assertTrue(jwtTokenProvider.isExistsRefreshToken(refreshTokenValue));
	}
}
