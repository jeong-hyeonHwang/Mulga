package com.example.mugbackend.user.service;

import org.springframework.stereotype.Service;

import com.example.mugbackend.user.domain.User;
import com.example.mugbackend.user.dto.CustomUserDetails;
import com.example.mugbackend.user.dto.UserCreateDto;
import com.example.mugbackend.user.exception.ActiveUserNotFoundException;
import com.example.mugbackend.user.exception.UserAlreadyExistsException;
import com.example.mugbackend.user.repository.UserRepository;
import com.example.mugbackend.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public User findUserById(String id) {
		return userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public User findActiveUserById(String id) {
		return userRepository.findByIdAndIsWithdrawnFalse(id)
			.orElseThrow(ActiveUserNotFoundException::new);
	}

	public User signUp(CustomUserDetails userDetails, UserCreateDto dto) {
		User user = dto.toEntity();
		user.setId(userDetails.id());

		if(userRepository.existsById(user.getId())) {
			throw new UserAlreadyExistsException();
		}

		return userRepository.save(user);
	}
}
