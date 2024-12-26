package com.yash.FinanceTracker.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class MonthlyAmountDTO {

    private Integer year;
    private String monthName;
    private Long totalAmount;
}
