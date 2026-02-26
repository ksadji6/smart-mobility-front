package com.esmt.front.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TripResponse {
    private String tripRef;
    private String transportType;
    private String origin;
    private String destination;
    private BigDecimal amountCharged;
    private String status;
    private boolean fallbackFare;
    private LocalDateTime startTime;
}
