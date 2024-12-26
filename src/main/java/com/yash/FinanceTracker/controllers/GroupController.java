package com.yash.FinanceTracker.controllers;


import com.yash.FinanceTracker.constants.UserRoles;
import com.yash.FinanceTracker.domain.Group;
import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.dto.GroupDTO;
import com.yash.FinanceTracker.dto.GroupDetailsDTO;
import com.yash.FinanceTracker.services.group.GroupService;
import com.yash.FinanceTracker.services.group.GroupServiceImpl;
import com.yash.FinanceTracker.services.user.UserService;
import com.yash.FinanceTracker.services.user.UserServiceImpl;
import com.yash.FinanceTracker.utils.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;


    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(@Valid @RequestBody GroupDTO groupDTO)
    {
        try
        {
            String userId= "9876544321";
            Optional<User> user= userService.findUserByUserId(userId);
            if(user.isPresent())
            {
                User user1 = user.get();
                if(!user1.getUserRolesSet().contains(UserRoles.PREMIUM_USER))
                {
                    return new ResponseEntity<>(new RestResponse<>(null,false,"User not having required permissions",403,null), HttpStatus.FORBIDDEN);
                }
                else
                {
                    Group createdGroup= groupService.saveGroup(groupDTO,userId, user1);
                    return ResponseEntity.ok(new RestResponse<>(createdGroup,true,null,null,null));
                }
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new RestResponse<>(null,false,"Something went wrong."+ e.getMessage(),500,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new RestResponse<>(null,false,"Something went wrong.",500,null),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId)
    {
        try
        {
            String userId="9876544321";
            if(groupService.checkUserIsGroupAdmin(userId,groupId))
            {
                Integer totalCount= groupService.deleteGroup(userId,groupId);
                return ResponseEntity.ok(new RestResponse<>("Total Users Removed: "+(totalCount-1),true,null,null,null));
            }
            else
            {
                return new ResponseEntity<>(new RestResponse<>(null,false,"User not  having required permissions",403,null),HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new RestResponse<>(null,false,"Something went wrong. "+ e.getMessage(),500,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-groups")
    public ResponseEntity<?> getGroups(@RequestParam String userId)
    {
        try
        {
            List<GroupDetailsDTO> groupDetailsList= groupService.getGroupDetails(userId);
            return ResponseEntity.ok(new RestResponse<>(groupDetailsList,true,null,null,null));
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(new RestResponse<>(null,false,"Something went wrong"+ e.getMessage(),500,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
