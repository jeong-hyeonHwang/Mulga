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
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.transaction.service.TransactionService;
import java.time.LocalDate;
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

	public User signUp(CustomUserDetails userDetails, UserCreateDto dto) {
		User user = dto.toEntity();
		user.setId(userDetails.id());

		if(userRepository.existsById(user.getId())) {
			throw new UserAlreadyExistsException();
		}

		return userRepository.save(user);
	}

    public int getRemainingBudget(String userId) {
        int budget = getBudget(userId);
        int monthTotal =transactionService.getMonthTotal(userId,
                LocalDate.now().getYear(), LocalDate.now().getMonthValue());
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
}
