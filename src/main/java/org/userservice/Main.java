package org.userservice;

import org.userservice.dao.UserDAO;
import org.userservice.dao.UserDAOImpl;
import org.userservice.service.UserService;
import org.userservice.service.UserServiceImpl;
import org.userservice.console.ConsoleUI;
import org.userservice.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        try {

            HibernateUtil.getSessionFactory();


            UserDAO userDAO = new UserDAOImpl();
            UserService userService = new UserServiceImpl(userDAO);
            ConsoleUI consoleUI = new ConsoleUI(userService);


            consoleUI.start();

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        } finally {

            HibernateUtil.shutdown();
        }
    }
}