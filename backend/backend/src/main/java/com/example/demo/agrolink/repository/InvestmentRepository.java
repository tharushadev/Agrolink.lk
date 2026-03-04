package com.example.demo.agrolink.repository;

import com.example.demo.agrolink.model.Investment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InvestmentRepository extends MongoRepository<Investment, String> {
    List<Investment> findByProjectId(String projectId);

    List<Investment> findByInvestorId(String investorId);
}
