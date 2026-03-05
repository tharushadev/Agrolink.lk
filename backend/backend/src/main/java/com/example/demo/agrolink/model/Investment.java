package com.example.demo.agrolink.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investment {
    @Id
    private String id;
    private String projectId;
    private String investorId;
    private Double amount;
    private Integer quantity;
    private Double unitPrice;
    private Integer totalUnits;
    private Integer availableUnits;

    @Builder.Default
    private Date timestamp = new Date();
}
