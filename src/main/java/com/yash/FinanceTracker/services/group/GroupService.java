package com.yash.FinanceTracker.services.group;

import com.yash.FinanceTracker.domain.Group;
import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.dto.GroupDTO;
import com.yash.FinanceTracker.dto.GroupDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface GroupService {

    Group saveGroup(GroupDTO groupDTO, String  userId, User user);

    List<GroupDetailsDTO> getGroupDetails(String userId);

    Boolean checkUserIsGroupAdmin(String userId, String groupId);

    Integer deleteGroup(String userId, String groupId);
}
