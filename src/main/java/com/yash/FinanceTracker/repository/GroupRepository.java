package com.yash.FinanceTracker.repository;

import com.yash.FinanceTracker.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    @Query(value = "SELECT \n" +
            "    ugm.group_id, \n" +
            "    ug.groupName,\n" +
            "    CASE \n" +
            "        WHEN ug.groupAdminId = ugm.user_id THEN ug.groupAdminId\n" +
            "        ELSE NULL \n" +
            "    END AS groupAdminId\n" +
            "FROM \n" +
            "    user_groups ug\n" +
            "JOIN \n" +
            "    user_group_mapping ugm \n" +
            "ON \n" +
            "    ug.group_id = ugm.group_id\n" +
            "WHERE \n" +
            "    ug.groupAdminId = :user_id OR ugm.user_id = :user_id", nativeQuery = true)
    public List<Object[]> getGroupDetails(@Param("user_id") String user_id);


    @Query(value = "SELECT CASE WHEN groupAdminId= :user_id THEN TRUE ELSE FALSE END AS isAdmin from user_groups where group_id= :group_id", nativeQuery = true)
    public boolean isGroupAdmin(@Param("group_id") String group_id, @Param("user_id") String user_id);

    @Query(value = "DELETE from user_group_mapping where group_id= :group_id",nativeQuery = true)
    @Transactional
    @Modifying
    public Integer deleteUsersFromMapping(@Param("group_id") String group_id);

    @Query(value = "DELETE from user_groups where group_id= :group_id",nativeQuery = true)
    @Transactional
    @Modifying
    public Integer deleteAdminFromGroups(@Param("group_id") String group_id);

}
