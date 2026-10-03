package com.secureflow.secureflow_backend.user.service;

import com.secureflow.secureflow_backend.user.dto.*;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    UserResponse changeRole(Long id, ChangeRoleRequest request);

    UserResponse changeStatus(
            Long id,
            ChangeStatusRequest request
    );

}
