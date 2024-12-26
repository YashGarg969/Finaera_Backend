package com.yash.FinanceTracker.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InvestmentDTO {

    private Long id;

    @NotNull(message = "Amount cannot be null")
    private Long investedAmount;

    @NotNull(message = "InvestmentType cannot be null")
    private String investmentType;

    @NotNull(message = "Date cannot be null")
    private LocalDate investmentDate;

    private String investmentDesc;

}
