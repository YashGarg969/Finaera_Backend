package com.yash.FinanceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DayWiseAmountDTO {

    LocalDate date;
    String day;
    Long dayWiseAmount;

}
