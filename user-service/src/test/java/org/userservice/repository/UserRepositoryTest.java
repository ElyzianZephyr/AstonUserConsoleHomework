package org.userservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.userservice.entity.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UserRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void saveAndFindById_Success() {
        User user = User.builder().name("TestUser").email("test@example.com").age(25).build();

        User savedUser = userRepository.save(user);
        Optional<User> found = userRepository.findById(savedUser.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("TestUser");
    }

    @Test
    void findAll_ReturnsAllSavedUsers() {
        userRepository.save(User.builder().name("User1").email("u1@example.com").age(20).build());
        userRepository.save(User.builder().name("User2").email("u2@example.com").age(30).build());

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    void existsByEmail_ReturnsTrueWhenExists() {
        userRepository.save(User.builder().name("User1").email("exist@example.com").age(20).build());

        boolean exists = userRepository.existsByEmail("exist@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void delete_RemovesUser() {
        User user = User.builder().name("ToDelete").email("delete@example.com").age(25).build();
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isEmpty();
    }
}