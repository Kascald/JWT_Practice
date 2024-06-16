package com.onboarding.preonboarding.exception;


import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@RequiredArgsConstructor
public class DefaultErrorResponse {
	private int status;
	private String code;
	private String description;

	public static ResponseEntity<DefaultErrorResponse> toDefaultErrorResponse(
			UserServiceErrorCode e) {
		return ResponseEntity.status(e.getHttpStatus())
				.body(DefaultErrorResponse.builder()
						      .status(e.getHttpStatus().value())
						      .code(e.name())
						      .description(e.getMessage())
						      .build()
				     );
	}
}
