package com.esmt.front.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PassResponse {
    private Long id;
    private String passNumber;
    private String passType;
    private String status;
    private LocalDateTime issuedAt;
}
