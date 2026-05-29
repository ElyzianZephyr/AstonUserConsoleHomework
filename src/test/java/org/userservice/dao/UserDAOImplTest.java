package org.userservice.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.userservice.entity.User;
import org.userservice.util.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class UserDAOImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDAO userDAO;

    @BeforeAll
    static void setupHibernate() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", postgres.getJdbcUrl());
        properties.put("hibernate.connection.username", postgres.getUsername());
        properties.put("hibernate.connection.password", postgres.getPassword());
        properties.put("hibernate.hbm2ddl.auto", "create-drop");

        HibernateUtil.getSessionFactory(properties);
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.shutdown();
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            tx.commit();
        }
    }



    @Test
    void saveAndFindById_Success() {
        User user = new User("TestUser", "test@example.com", 25);

        userDAO.save(user);
        Optional<User> found = userDAO.findById(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("TestUser");
    }

    @Test
    void findAll_ReturnsAllSavedUsers() {
        userDAO.save(new User("User1", "u1@example.com", 20));
        userDAO.save(new User("User2", "u2@example.com", 30));

        List<User> users = userDAO.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    void update_ChangesUserData() {
        User user = new User("OldName", "old@example.com", 20);
        userDAO.save(user);

        user.setName("NewName");
        userDAO.update(user);

        Optional<User> updatedUser = userDAO.findById(user.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("NewName");
    }

    @Test
    void delete_RemovesUser() {
        User user = new User("ToDelete", "delete@example.com", 25);
        userDAO.save(user);

        userDAO.delete(user.getId());

        Optional<User> deletedUser = userDAO.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }
}