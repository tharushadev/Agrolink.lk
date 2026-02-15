package com.example.demo.agrolink.repository;

import com.example.demo.agrolink.model.FarmerProject;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FarmerProjectRepository extends MongoRepository<FarmerProject, String> {
    // Custom query: Find all projects belonging to one farmer
    List<FarmerProject> findByFarmerId(String farmerId);
}
