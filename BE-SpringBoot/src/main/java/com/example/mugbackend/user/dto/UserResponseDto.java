package com.example.mugbackend.user.dto;

import com.example.mugbackend.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	@Id
	private String id;
	private String name;
	private String email;
	private Integer budget;
	private Boolean isWithdrawn;
	private Boolean receivesNotification;
	private String createdAt;
	private String withdrawnAt;
}
