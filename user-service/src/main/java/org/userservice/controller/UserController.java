package org.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.userservice.dto.UserRequest;
import org.userservice.dto.UserResponse;
import org.userservice.hateoas.UserModelAssembler;
import org.userservice.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями (CRUD операции)")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userAssembler;

    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Добавляет нового пользователя в систему и возвращает его данные вместе с HATEOAS ссылками.")
    public ResponseEntity<EntityModel<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userAssembler.toModel(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    public ResponseEntity<EntityModel<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse response = userService.findUser(id);
        return ResponseEntity.ok(userAssembler.toModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные пользователя")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(userAssembler.toModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей")
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAllUsers() {
        List<EntityModel<UserResponse>> users = userService.getAllUsers().stream()
                .map(userAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<UserResponse>> collectionModel = CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}