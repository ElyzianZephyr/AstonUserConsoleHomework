package org.userservice;

import org.junit.jupiter.api.Test;

class EnvTest {
    @Test
    void checkDocker() {
        System.out.println("========== РЕЗУЛЬТАТЫ ==========");
        System.out.println("DOCKER_HOST: " + System.getenv("DOCKER_HOST"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("=================================");
    }
}