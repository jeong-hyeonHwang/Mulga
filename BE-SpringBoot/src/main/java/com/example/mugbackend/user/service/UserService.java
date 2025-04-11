package com.example.mugbackend.user.service;

import com.example.mugbackend.user.dto.*;
import org.springframework.stereotype.Service;
import com.example.mugbackend.user.domain.User;
import com.example.mugbackend.user.exception.ActiveUserNotFoundException;
import com.example.mugbackend.user.exception.UserAlreadyExistsException;
import com.example.mugbackend.user.repository.UserRepository;
import com.example.mugbackend.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.transaction.service.TransactionService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AnalysisRepository analysisRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

	public User findUserById(String id) {
		return userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public User findActiveUserById(String id) {
		return userRepository.findByIdAndIsWithdrawnFalse(id)
			.orElseThrow(ActiveUserNotFoundException::new);
	}

	public UserResponseDto signUp(CustomUserDetails userDetails, UserCreateDto dto) {
		User user = dto.toEntity();
		user.setId(userDetails.id());

		if(userRepository.existsById(user.getId())) {
			throw new UserAlreadyExistsException();
		}

		userRepository.save(user);

		return convertToResponseDto(user);
	}

	public UserResponseDto convertToResponseDto(User user) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		return UserResponseDto.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.budget(user.getBudget())
				.isWithdrawn(user.getIsWithdrawn())
				.receivesNotification(user.getReceivesNotification())
				.createdAt(user.getCreatedAt() == null ? "" : user.getCreatedAt().format(formatter))
				.withdrawnAt(user.getWithdrawnAt() == null ? "" : user.getWithdrawnAt().format(formatter))
				.build();
	}

    public int calRemainingBudget(String userId, int monthTotal) {
        int budget = getBudget(userId);
        int remainingBudget = budget - monthTotal;
        if(remainingBudget > 0) {
            return remainingBudget;
        }
        return 0;
    }

    public int getBudget(String userId) {
        return userRepository.findById(userId)
                .map(User::getBudget)
                .filter(Objects::nonNull)
                .orElse(0);
    }

	public UserDetailDto updateUser(CustomUserDetails userDetails, UserUpdateDto dto) {
		User user = userDetails.toEntity();
		dto.applyChangesToUser(user);

		userRepository.save(user);

		return UserDetailDto.of(user);
	}
}
