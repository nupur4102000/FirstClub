package com.firstclub.membership.dto;

public record UserContextDto(
    String userId, 
    int monthlyOrderCount, 
    double monthlyOrderValue, 
    String cohort
) {}