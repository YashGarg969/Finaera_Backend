package com.yash.FinanceTracker.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseDTO {

    private Long id;

    @NotNull(message = "Expense type cannot be blank")
    private String expType;

    @NotNull(message = "Expense amount cannot be blank")
    private Integer expAmount;

    @NotNull(message = "Expense date cannot be blank")
    private LocalDate date;
    private String desc;
}
