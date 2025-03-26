package com.example.mugbackend.common.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.mugbackend.common.exception.ErrorCode;
import com.example.mugbackend.common.exception.MulgaException;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	private String code;
	private Map<String, List<String>> errors;

	public static ResponseEntity<Object> toResponseEntity(MulgaException exception) {
		ErrorCode errorCode = exception.getErrorCode();

		return ResponseEntity
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(
				ErrorResponse.builder()
					.code(errorCode.getCode())
					.build()
			);
	}

	public static ResponseEntity<Object> toResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(
				ErrorResponse.builder()
					.code(errorCode.getCode())
					.build()
			);
	}

	public static ResponseEntity<Object> toResponseEntity(HttpStatus status, String detail) {
		return ResponseEntity
			.status(status)
			.contentType(MediaType.APPLICATION_JSON)
			.body(
				ErrorResponse.builder()
					.code(String.format("COMMON_%s", status.value()))
					.build()
			);
	}

	public static ResponseEntity<Object> toResponseEntity(ErrorCode errorCode, Map<String, List<String>> errors) {
		Map<String, String> message = new HashMap<>();
		message.put("code", errorCode.getCode());

		return ResponseEntity
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(
				ErrorResponse.builder()
					.code(errorCode.getCode())
					.errors(errors)
					.build()
			);
	}

}
