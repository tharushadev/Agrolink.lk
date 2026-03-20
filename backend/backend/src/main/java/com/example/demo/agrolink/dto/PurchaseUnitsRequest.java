package com.example.demo.agrolink.dto;

import lombok.Data;

@Data
public class PurchaseUnitsRequest {
    private String projectId;
    private String investorId;
    private Integer quantity;
}