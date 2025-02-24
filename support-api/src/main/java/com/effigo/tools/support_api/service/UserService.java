package com.effigo.tools.support_api.service;


import java.util.List;

import com.effigo.tools.support_api.dto.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    UserDTO getUserByEmail(String email);

    UserDTO loginUser(String userName, String password);
}
