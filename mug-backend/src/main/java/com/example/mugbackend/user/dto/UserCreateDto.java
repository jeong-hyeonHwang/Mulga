package com.example.mugbackend.user.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.example.mugbackend.user.domain.User;

public record UserCreateDto (
	@NotBlank(message = "Name is required")
	String name,

	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	String email,

	@NotNull(message = "Budget is required")
	@Min(value = 0, message = "Budget must be a positive number")
	Integer budget
) {
	public User toEntity() {
		return User.builder()
			.name(this.name)
			.email(this.email)
			.budget(this.budget)
			.isWithdrawn(false)
			.receivesNotification(true)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
