package com.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "payment_accounts")
public class PaymentAccount {

    @Id
    private String id;

    private String userId;
    private String bankName;
    private String accountNumber;
    private Date linkedAt = new Date();

    public PaymentAccount() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(Date linkedAt) {
        this.linkedAt = linkedAt;
    }
}
