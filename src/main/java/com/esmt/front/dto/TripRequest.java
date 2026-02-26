package com.esmt.front.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TripRequest {
    private Long userId;
    private String passNumber;
    private String transportType;
    private String origin;
    private String destination;
}