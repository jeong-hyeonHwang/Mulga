package com.example.mugbackend.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.mugbackend.user.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findById(String id);
	Optional<User> findByIdAndIsWithdrawnFalse(String id);
}
