package com.example.mugbackend.user.repository;

import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

}