package org.userservice.mapper;

import org.springframework.stereotype.Component;
import org.userservice.dto.UserRequest;
import org.userservice.dto.UserResponse;
import org.userservice.entity.User;

@Component
public class UserMapper {


    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getEmail()
        );
    }

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }
        return User.builder()
                .name(request.name().trim())
                .age(request.age())
                .email(request.email().trim())
                .build();
    }

    public void updateEntityFromRequest(UserRequest request, User user) {
        if (request == null || user == null) {
            return;
        }
        user.setName(request.name().trim());
        user.setAge(request.age());
        user.setEmail(request.email().trim());
    }
}