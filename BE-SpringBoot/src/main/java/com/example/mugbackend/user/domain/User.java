package com.example.mugbackend.user.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
	@Id
	private String id;
	private String name;
	private String email;
	private Integer budget;
	private Boolean isWithdrawn;
	private Boolean receivesNotification;
	private LocalDateTime createdAt;
	private LocalDateTime withdrawnAt;
}
