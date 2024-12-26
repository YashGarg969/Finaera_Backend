package com.yash.FinanceTracker.repository;

import com.yash.FinanceTracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query(value = "SELECT * FROM user WHERE userName = :userName", nativeQuery = true)
    User findUserByUserName(@Param("userName") String userName);

    @Query(value = "SELECT * from user where userId= :userId", nativeQuery = true)
    User findUserByUserId(@Param("userId") String userId);


}
