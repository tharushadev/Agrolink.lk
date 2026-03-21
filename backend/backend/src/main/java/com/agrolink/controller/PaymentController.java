package com.agrolink.controller;

import com.agrolink.model.PaymentAccount;
import com.agrolink.repository.PaymentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPaymentAccount(@PathVariable String userId) {
        return paymentAccountRepository.findByUserId(userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(Map.of(
                        "userId", userId,
                        "linked", false
                )));
    }

    @PostMapping("/link-bank-account")
    public ResponseEntity<?> linkBankAccount(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String bankName = request.get("bankName");
        String accountNumber = request.get("accountNumber");

        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().body("userId is required");
        }
        if (bankName == null || bankName.isBlank()) {
            return ResponseEntity.badRequest().body("bankName is required");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            return ResponseEntity.badRequest().body("accountNumber is required");
        }

        Optional<PaymentAccount> existing = paymentAccountRepository.findByUserId(userId);
        PaymentAccount account = existing.orElseGet(PaymentAccount::new);
        account.setUserId(userId);
        account.setBankName(bankName);
        account.setAccountNumber(accountNumber);

        PaymentAccount saved = paymentAccountRepository.save(account);
        return ResponseEntity.ok(Map.of(
                "message", "Bank account linked",
                "paymentAccount", saved
        ));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, Object> request) {
        String userId = request.get("userId") == null ? null : request.get("userId").toString();
        Double amount = request.get("amount") == null ? null : Double.valueOf(request.get("amount").toString());

        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().body("userId is required");
        }
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().body("amount must be > 0");
        }

        // MVP simulation: we just confirm the request.
        // A real implementation would calculate available wallet balance and create a withdrawal transaction.
        return ResponseEntity.ok(Map.of(
                "message", "Withdrawal request submitted",
                "userId", userId,
                "amount", amount,
                "currency", "LKR",
                "status", "PROCESSING"
        ));
    }
}
