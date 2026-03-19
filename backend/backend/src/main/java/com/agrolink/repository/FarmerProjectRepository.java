package com.agrolink.repository;

import com.agrolink.model.FarmerProject;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FarmerProjectRepository extends MongoRepository<FarmerProject, String> {
    List<FarmerProject> findByFarmerId(String farmerId);
    List<FarmerProject> findByStatus(FarmerProject.ProjectStatus status);
}