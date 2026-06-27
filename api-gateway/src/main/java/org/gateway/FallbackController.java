package org.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/users")
    public Mono<String> userServiceFallback() {
        return Mono.just("Сервис пользователей временно недоступен. Пожалуйста, попробуйте позже.");
    }
}