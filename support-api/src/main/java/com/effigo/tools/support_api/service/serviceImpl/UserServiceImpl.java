package com.effigo.tools.support_api.service.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effigo.tools.support_api.dto.UserDTO;
import com.effigo.tools.support_api.exception.DuplicateResourceException;
import com.effigo.tools.support_api.exception.InvalidCredentialsException;
import com.effigo.tools.support_api.exception.ResourceNotFoundException;
import com.effigo.tools.support_api.model.User;
import com.effigo.tools.support_api.repository.UserRepository;
import com.effigo.tools.support_api.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new DuplicateResourceException("User with email " + userDTO.getEmail() + " already exists");
        }

        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        modelMapper.map(userDTO, existingUser);
        existingUser.setId(id);
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO loginUser(String userName, String password) {
        User user = userRepository.findByUserNameAndPassword(userName, password);
        if (user == null) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return modelMapper.map(user, UserDTO.class);
    }
}
