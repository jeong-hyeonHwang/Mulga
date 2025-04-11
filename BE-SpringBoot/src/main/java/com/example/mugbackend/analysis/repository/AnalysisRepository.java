package com.example.mugbackend.analysis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.mugbackend.analysis.domain.Analysis;

@Repository
public interface AnalysisRepository extends MongoRepository<Analysis, String>, CustomAnalysisRepository {

}
