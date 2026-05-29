package org.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.userservice.dao.UserDAO;
import org.userservice.entity.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_Success() {
        User user = userService.createUser("Alice", 25, "alice@example.com");

        assertThat(user.getName()).isEqualTo("Alice");
        assertThat(user.getAge()).isEqualTo(25);
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        verify(userDAO).save(any(User.class));
    }

    @Test
    void createUser_InvalidAge_ThrowsException() {
        assertThatThrownBy(() -> userService.createUser("Alice", -5, "alice@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    void findUser_UserExists() {
        User mockUser = new User("Bob", "bob@example.com", 30);
        when(userDAO.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.findUser(1L);

        assertThat(result).isPresent().contains(mockUser);
    }

    @Test
    void findUser_UserDoesNotExist() {
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUser(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void updateUser_Success() {
        User existingUser = new User("OldName", "old@example.com", 20);
        when(userDAO.findById(1L)).thenReturn(Optional.of(existingUser));

        User updatedUser = userService.updateUser(1L, "NewName", 25, "new@example.com");

        assertThat(updatedUser.getName()).isEqualTo("NewName");
        assertThat(updatedUser.getAge()).isEqualTo(25);
        assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
        verify(userDAO).update(existingUser);
    }

    @Test
    void updateUser_NotFound_ThrowsException() {
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(1L, "NewName", 25, "new@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        User mockUser = new User("Charlie", "charlie@example.com", 35);
        when(userDAO.findById(1L)).thenReturn(Optional.of(mockUser));

        boolean result = userService.deleteUser(1L);

        assertThat(result).isTrue();
        verify(userDAO).delete(1L);
    }

    @Test
    void deleteUser_NotFound() {
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(1L);

        assertThat(result).isFalse();
        verify(userDAO, never()).delete(anyLong());
    }

    @Test
    void getAllUsers_ReturnsList() {
        List<User> users = List.of(
                new User("User1", "u1@example.com", 20),
                new User("User2", "u2@example.com", 25)
        );
        when(userDAO.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2).isEqualTo(users);
    }
}