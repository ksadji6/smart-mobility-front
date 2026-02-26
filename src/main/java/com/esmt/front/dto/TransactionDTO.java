package com.esmt.front.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private String txnRef;
    private BigDecimal amount;
    private String type; // DEBIT ou CREDIT
    private String status; // SUCCESS, FAILED
    private String tripRef;
    private LocalDateTime createdAt;
}