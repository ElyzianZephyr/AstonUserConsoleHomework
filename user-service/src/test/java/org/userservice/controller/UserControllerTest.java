package org.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.userservice.dto.UserRequest;
import org.userservice.dto.UserResponse;
import org.userservice.hateoas.UserModelAssembler;
import org.userservice.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserModelAssembler userAssembler;

    @Test
    void createUser_ReturnsCreatedUser() throws Exception {
        UserRequest request = new UserRequest("Alice", 25, "alice@example.com");
        UserResponse response = new UserResponse(1L, "Alice", 25, "alice@example.com");


        EntityModel<UserResponse> entityModel = EntityModel.of(response);

        when(userService.createUser(any(UserRequest.class))).thenReturn(response);
        when(userAssembler.toModel(any(UserResponse.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                // HATEOAS оборачивает объект в поле "content"
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getUser_ReturnsUser() throws Exception {
        UserResponse response = new UserResponse(1L, "Bob", 30, "bob@example.com");
        EntityModel<UserResponse> entityModel = EntityModel.of(response);

        when(userService.findUser(1L)).thenReturn(response);
        when(userAssembler.toModel(any(UserResponse.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void createUser_InvalidData_ReturnsBadRequest() throws Exception {
        UserRequest invalidRequest = new UserRequest("", 200, "invalid-email");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.age").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void deleteUser_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }
}