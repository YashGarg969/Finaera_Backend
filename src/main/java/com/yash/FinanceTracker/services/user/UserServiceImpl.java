package com.yash.FinanceTracker.services.user;

import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean findUserById(Long id)
    {
        //return userRepository.findById(id).isPresent();
        return true;
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    @Override
    public Optional<User> findUserByUserId(String userId) {
        return Optional.ofNullable(userRepository.findUserByUserId(userId));
    }

}
