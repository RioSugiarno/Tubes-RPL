package com.Tubes.code.Repository;

import com.Tubes.code.Entity.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user) throws Exception;
    Optional<User> find(String username, String password) throws Exception;
}
