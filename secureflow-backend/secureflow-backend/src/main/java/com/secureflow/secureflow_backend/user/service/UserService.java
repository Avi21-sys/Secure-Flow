package com.secureflow.secureflow_backend.user.service;

import com.secureflow.secureflow_backend.user.dto.ChangeRoleRequest;
import com.secureflow.secureflow_backend.user.dto.CreateUserRequest;
import com.secureflow.secureflow_backend.user.dto.UpdateUserRequest;
import com.secureflow.secureflow_backend.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    UserResponse changeRole(Long id, ChangeRoleRequest request);

    UserResponse changeStatus(Long id, boolean enabled);

    void deleteUser(Long id);
}
