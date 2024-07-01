package com.onboarding.preonboarding.SecureLogin;


import com.onboarding.preonboarding.entity.RefreshToken;
import com.onboarding.preonboarding.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Ref;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JWTTokenProvider {

	@Value("${myJwtRandomKeyHashed}")
	private String jwtSecretKey;

	private SecretKey mySecretKey;
	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${accessTokenExpiration}")
	private long ACCESS_EXPIRATION_PERIOD;   //10 min => millseeconds
	@Value("${refreshTokenExpiration}")
	private long REFRESH_EXPIRATION_PERIOD;   //1 hour => millseeconds



	public JWTTokenProvider(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;

	}

	@PostConstruct
	public void init() {
		byte[] keyBytes = jwtSecretKey.getBytes();
		mySecretKey = Keys.hmacShaKeyFor(keyBytes);
	}

//	private SecretKey secretKeyEncode() {
//		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
//	}

	public String generateToken(String username, String userRealName
			, List<String> userRole, Long expriation) {
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		Instant now = ZonedDateTime.now(zoneId).toInstant();
		Date issuedAt = Date.from(now);
		Date expiration = Date.from(now.plusMillis(expriation));
		Map<String, Object> claims = new HashMap<>();
		claims.put("userRealName", userRealName);
		claims.put("userRole", userRole);

		return Jwts.builder()
				.subject(username)
				.claims(claims)
				.signWith(mySecretKey)
				.issuedAt(issuedAt)
				.expiration(expiration)
				.encodePayload(true)
				.compact();
	}

	public String createAccessToken(String username, String userRealName , List<String> userRole) {
		return generateToken(username, userRealName, userRole, ACCESS_EXPIRATION_PERIOD);
	}

	public String createRefreshToken(String username, String userRealName , List<String> userRole) {
		return generateToken(username, userRealName, userRole, REFRESH_EXPIRATION_PERIOD);
	}

	public void saveRefreshToken(String refreshToken) {
		dbSaveRT(refreshToken);
	}

	private void dbSaveRT(String refreshToken) {
		refreshTokenRepository.save(convertToRefreshTokenEntity(refreshToken));
	}

	private RefreshToken convertToRefreshTokenEntity(String refreshToken) {
		Claims refreshClaims = parsePayloadFromToken(refreshToken);
		RefreshToken tokenEntity = new RefreshToken();

		tokenEntity.setRefreshToken(refreshToken);
		tokenEntity.setSubject(refreshClaims.getSubject());
		tokenEntity.setExpiration(refreshClaims.getExpiration());

		return tokenEntity;
	}

	public Claims parsePayloadFromToken(String token) throws JwtException {
		return Jwts.parser()
				.verifyWith(mySecretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	public String getUserEmailFromToken(String token) throws JwtException {
		return parsePayloadFromToken(token).getSubject();
	}


	public boolean validateToken(String token) {
		try {
			parsePayloadFromToken(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public boolean isTokenNotExpiration(String token) {
		Claims claims = parsePayloadFromToken(token);
		Date expiration = claims.getExpiration();

		if  (compareExpirationDate(expiration)) {
			return false;
		}else {
			return true;
		}
	}

	private boolean compareExpirationDate(Date expiration) {
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		Instant now = ZonedDateTime.now(zoneId).toInstant();
		Date confirmTime = Date.from(now);

		return confirmTime.after(expiration);
	}


	public void setAuthorizationHeaderForAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader("authorization", "bearer "+ accessToken);
	}

	public void setAuthorizationHeaderForRefreshToken(HttpServletResponse response, String refreshToken) {
		response.setHeader("authorization", "bearer "+ refreshToken);
	}

	public boolean isExistsRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken).isPresent();
	}


}
