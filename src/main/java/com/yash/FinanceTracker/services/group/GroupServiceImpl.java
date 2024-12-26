package com.yash.FinanceTracker.services.group;

import com.yash.FinanceTracker.domain.Group;
import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.dto.GroupDTO;
import com.yash.FinanceTracker.dto.GroupDetailsDTO;
import com.yash.FinanceTracker.repository.GroupRepository;
import com.yash.FinanceTracker.services.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group saveGroup(GroupDTO groupDTO, String userId, User user) {
        Group newGroup= new Group();
        newGroup.setGroupAdminId(userId);
        Set<User> members= new HashSet<>();

        members.add(user);

        newGroup.setGroupMembers(members);
        return createGroup(newGroup,groupDTO);
    }

    @Override
    public List<GroupDetailsDTO> getGroupDetails(String userId) {

        List<Object[]> results= groupRepository.getGroupDetails(userId);

        List<GroupDetailsDTO> groupDetailsList= new ArrayList<>();

        for(Object[] res: results)
        {
            String groupId= res[0].toString();
            String groupName= res[1].toString();
            GroupDetailsDTO groupDetailsDTO= new GroupDetailsDTO();
            groupDetailsDTO.setGroupId(groupId);
            groupDetailsDTO.setGroupName(groupName);
            if(res[2]==null)
            {
                groupDetailsDTO.setGroupAdminId(null);
            }
            else
            {
                groupDetailsDTO.setGroupAdminId(res[2].toString());
            }
            groupDetailsList.add(groupDetailsDTO);
        }
        return groupDetailsList;
    }

    @Override
    public Boolean checkUserIsGroupAdmin(String userId, String groupId) {
        return groupRepository.isGroupAdmin(groupId,userId);
    }

    @Transactional
    @Override
    public Integer deleteGroup(String userId, String groupId) {
        Integer deletedUsersFromMapping= groupRepository.deleteUsersFromMapping(groupId);
        Integer deletedAdminFromGroup= groupRepository.deleteAdminFromGroups(groupId);
        return deletedUsersFromMapping+ deletedAdminFromGroup;
    }

    private Group createGroup(Group group, GroupDTO groupDTO)
    {
        group.setGroupName(groupDTO.getGroupName());
        group.setDateCreated(groupDTO.getDateCreated());
        return groupRepository.save(group);
    }
}
