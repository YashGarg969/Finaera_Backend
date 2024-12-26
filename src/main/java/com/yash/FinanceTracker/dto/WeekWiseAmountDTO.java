package com.yash.FinanceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeekWiseAmountDTO {

    Integer weekNumber;
    Long totalAmount;
}
