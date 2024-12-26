package com.yash.FinanceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalAmountDTO {

    private Long totalCurrMonthAmount;
    private Long totalCurrYearAmount;

}
