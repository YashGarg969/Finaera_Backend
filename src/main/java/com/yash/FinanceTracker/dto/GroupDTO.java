package com.yash.FinanceTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GroupDTO {

    @NotNull(message = "Group Name cannot be empty")
    private String groupName;

    @NotNull(message = "Date cannot be empty")
    private LocalDate dateCreated;

    private Boolean isGroupAdmin;
}
