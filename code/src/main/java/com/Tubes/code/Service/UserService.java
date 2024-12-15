package com.Tubes.code.Service;

import com.Tubes.code.Entity.User;
import com.Tubes.code.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User login(String username, String password) throws Exception {
        Optional<User> user = userRepository.find(username,password);
        return user.orElse(null);
    }
}
