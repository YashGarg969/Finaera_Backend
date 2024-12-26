package com.yash.FinanceTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeDTO {

    private String id;

    @NotNull(message = "Income source cannot be empty")
    private String incomeSrc;

    @NotNull(message = "Income amount cannot be empty")
    private Integer incomeAmount;

    @NotNull(message = "Income date cannot be empty")
    private LocalDate date;

    private String desc;
}
