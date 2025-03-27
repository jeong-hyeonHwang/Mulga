package com.example.mugbackend.user.service;

import org.springframework.stereotype.Service;

import com.example.mugbackend.user.domain.User;
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
}
