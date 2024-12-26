package com.yash.FinanceTracker.dto;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class GroupDetailsDTO {

    private String groupId;
    private String groupName;
    private String groupAdminId;
}
