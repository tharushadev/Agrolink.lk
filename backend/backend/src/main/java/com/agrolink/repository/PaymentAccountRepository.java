package com.agrolink.repository;

import com.agrolink.model.PaymentAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentAccountRepository extends MongoRepository<PaymentAccount, String> {
    Optional<PaymentAccount> findByUserId(String userId);
}
