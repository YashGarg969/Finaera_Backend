package com.yash.FinanceTracker.services.user;

import com.yash.FinanceTracker.domain.User;

import java.util.Optional;

public interface UserService {
    public Boolean findUserById(Long id);

    public User findUserByUserName(String userName);

    public Optional<User> findUserByUserId(String userId);
}
