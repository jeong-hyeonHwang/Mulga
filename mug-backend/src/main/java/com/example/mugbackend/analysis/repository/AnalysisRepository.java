package com.example.mugbackend.analysis.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.mugbackend.analysis.domain.Analysis;

public interface AnalysisRepository extends MongoRepository<Analysis, String> {

}
