package org.userservice.exeption;

import org.userservice.dao.UserDAO;
import org.userservice.dao.UserDAOImpl;
import org.userservice.entity.User;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
