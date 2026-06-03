package org.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.userservice.dto.UserRequest;
import org.userservice.dto.UserResponse;
import org.userservice.entity.User;
import org.userservice.mapper.UserMapper;
import org.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_Success() {
        UserRequest request = new UserRequest("Alice", 25, "alice@example.com");
        User user = User.builder().name("Alice").age(25).email("alice@example.com").build();
        User savedUser = User.builder().id(1L).name("Alice").age(25).email("alice@example.com").createdAt(LocalDateTime.now()).build();
        UserResponse expectedResponse = new UserResponse(1L, "Alice", 25, "alice@example.com");

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        UserResponse actualResponse = userService.createUser(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(userRepository).save(user);
    }

    @Test
    void findUser_UserExists() {
        User user = User.builder().id(1L).name("Bob").build();
        UserResponse expectedResponse = new UserResponse(1L, "Bob", 30, "bob@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        UserResponse actualResponse = userService.findUser(1L);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findUser_UserDoesNotExist_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUser(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateUser_Success() {
        UserRequest request = new UserRequest("NewName", 25, "new@example.com");
        User existingUser = User.builder().id(1L).name("OldName").build();
        User updatedUser = User.builder().id(1L).name("NewName").build();
        UserResponse expectedResponse = new UserResponse(1L, "NewName", 25, "new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(expectedResponse);

        UserResponse actualResponse = userService.updateUser(1L, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(userMapper).updateEntityFromRequest(request, existingUser);
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(IllegalArgumentException.class);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllUsers_ReturnsList() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        UserResponse response1 = new UserResponse(1L, "U1", 20, "u1@e.com");
        UserResponse response2 = new UserResponse(2L, "U2", 25, "u2@e.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toResponse(user1)).thenReturn(response1);
        when(userMapper.toResponse(user2)).thenReturn(response2);

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).hasSize(2).containsExactly(response1, response2);
    }
}