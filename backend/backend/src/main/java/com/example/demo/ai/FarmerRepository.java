package com.example.demo.ai;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FarmerRepository extends MongoRepository<Farmer, String> {
    // Basic CRUD operations are built-in automatically!
}