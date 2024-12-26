package com.yash.FinanceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YearlyAmountDTO {

    private Integer year;
    private Long totalYearlyAmount;
}
