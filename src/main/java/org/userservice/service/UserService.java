package org.userservice.service;

import org.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(String name, int age, String email);
    Optional<User> findUser(Long id);
    User updateUser(Long id, String name, int age, String email);
    boolean deleteUser(Long id);
    List<User> getAllUsers();
}
