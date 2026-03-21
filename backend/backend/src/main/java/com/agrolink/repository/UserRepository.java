package com.agrolink.repository;

import com.agrolink.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNicIgnoreCase(String nic);

    boolean existsByPhoneNumber(String phoneNumber);
}
