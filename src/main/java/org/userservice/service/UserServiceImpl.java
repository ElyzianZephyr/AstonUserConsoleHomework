package org.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.userservice.dao.UserDAO;
import org.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        if (userDAO == null) {
            throw new IllegalArgumentException("UserDAO не может быть null");
        }
        this.userDAO = userDAO;
    }

    @Override
    public User createUser(String name, int age, String email) {
        validateName(name);
        validateAge(age);
        validateEmail(email);

        User user = new User();
        user.setName(name.trim());
        user.setAge(age);
        user.setEmail(email.trim());

        userDAO.save(user);
        return user;
    }

    @Override
    public Optional<User> findUser(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return userDAO.findById(id);
    }

    @Override
    public User updateUser(Long id, String name, int age, String email) {
        if (id == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        validateName(name);
        validateAge(age);
        validateEmail(email);

        Optional<User> existingUserOpt = userDAO.findById(id);
        if (existingUserOpt.isEmpty()) {
            throw new IllegalArgumentException("Пользователь с ID=" + id + " не найден");
        }

        User existingUser = existingUserOpt.get();
        existingUser.setName(name.trim());
        existingUser.setAge(age);
        existingUser.setEmail(email.trim());

        userDAO.update(existingUser);
        return existingUser;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }

        Optional<User> userOpt = userDAO.findById(id);
        if (userOpt.isEmpty()) {
            return false;
        }

        userDAO.delete(id);
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // Вспомогательный метод для удобства
    public User findUserOrThrow(Long id) {
        return findUser(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID=" + id + " не найден"));
    }

    // ============ Методы валидации ============

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("Имя пользователя не может быть длиннее 100 символов");
        }
    }

    private void validateAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Возраст должен быть больше 0");
        }
        if (age > 150) {
            throw new IllegalArgumentException("Возраст не может быть больше 150 лет");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email должен содержать символ '@'");
        }
        if (email.trim().length() > 150) {
            throw new IllegalArgumentException("Email не может быть длиннее 150 символов");
        }
    }
}