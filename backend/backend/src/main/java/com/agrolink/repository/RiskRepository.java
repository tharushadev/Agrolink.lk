package com.agrolink.repository;

import com.agrolink.model.DistrictRisk;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RiskRepository extends MongoRepository<DistrictRisk, String> {
    Optional<DistrictRisk> findByDistrictAndSeason(String district, String season);
}