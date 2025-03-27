package com.example.mugbackend.user.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.mugbackend.user.domain.User;

import lombok.Builder;

@Builder
public record CustomUserDetails (
	String id,
	String name,
	Integer budget,
	String email,
	Boolean isWithdrawn,
	Boolean receivesNotification
) implements UserDetails
{
	public static CustomUserDetails of(User user) {
		return CustomUserDetails.builder()
			.id(user.getId())
			.name(user.getName())
			.budget(user.getBudget())
			.email(user.getEmail())
			.isWithdrawn(user.isWithdrawn())
			.receivesNotification(user.isReceivesNotification())
			.build();
	}

	public User toEntity() {
		return User.builder()
			.id(this.id)
			.name(this.name)
			.budget(this.budget)
			.email(this.email)
			.isWithdrawn(this.isWithdrawn)
			.receivesNotification(this.receivesNotification)
			.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.name;
	}
}
