package com.esmt.front.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreditRequest {
    private Long userId;
    private BigDecimal amount;
}
