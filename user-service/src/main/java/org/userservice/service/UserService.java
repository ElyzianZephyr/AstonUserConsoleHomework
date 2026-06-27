package org.userservice.service;

import org.userservice.dto.UserRequest;
import org.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse findUser(Long id);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    List<UserResponse> getAllUsers();
}