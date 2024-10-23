package com.example.shopapp.service;

import com.example.shopapp.dto.UpdateUserDTO;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;

    User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;
}
