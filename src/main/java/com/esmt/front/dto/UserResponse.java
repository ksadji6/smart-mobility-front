package com.esmt.front.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private int totalTrips;
    private PassResponse mobilityPass;
}