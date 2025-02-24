package com.effigo.tools.support_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.effigo.tools.support_api.dto.UserDTO;
import com.effigo.tools.support_api.exception.DuplicateResourceException;
import com.effigo.tools.support_api.exception.InvalidCredentialsException;
import com.effigo.tools.support_api.exception.ResourceNotFoundException;
import com.effigo.tools.support_api.model.User;
import com.effigo.tools.support_api.repository.UserRepository;
import com.effigo.tools.support_api.service.serviceImpl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private UserServiceImpl userService;

	private User user;
	private UserDTO userDTO;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setUserName("abhishek");
		user.setEmail("abhishek@gmail.com");
		user.setPassword("123");

		userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setUserName("abhishek");
		userDTO.setEmail("abhishek@gmail.com");
		userDTO.setPassword("123");
	}

	@Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
    }

	// negative test case
	@Test
    void testCreateUser_DuplicateEmail() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(userDTO);
        });

        assertEquals("User with email '" + userDTO.getEmail() + "' already exists.", exception.getMessage());
    }

	// positive
	@Test
	void testGetAllUsers() {
		List<User> users = Arrays.asList(user);

		when(userRepository.findAll()).thenReturn(users);
		when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

		List<UserDTO> result = userService.getAllUsers();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(userDTO.getEmail(), result.get(0).getEmail());
	}

	// positive
	@Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

	// negative
	@Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

	// positive

	@Test
	void testUpdateUser_Success() {
		User existingUser = new User();
		existingUser.setId(1L);
		existingUser.setUserName("abhishek");
		existingUser.setPassword("123");
		existingUser.setEmail("abhishek@gmail.com");
		existingUser.setCreatedOn(Instant.now());
		existingUser.setCreatedBy("Admin");

		when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
		doAnswer(invocation -> {
			UserDTO dto = invocation.getArgument(0);
			User user = invocation.getArgument(1);
			user.setUserName(dto.getUserName());
			user.setEmail(dto.getEmail());
			user.setPassword(dto.getPassword());
			return null;
		}).when(modelMapper).map(any(UserDTO.class), any(User.class));

		when(userRepository.save(any(User.class))).thenReturn(existingUser);
		when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);

		UserDTO updatedUser = userService.updateUser(1L, userDTO);

		assertNotNull(updatedUser);
		assertEquals(userDTO.getEmail(), updatedUser.getEmail());
	}

	// negative
	@Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(1L, userDTO);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

	// positive
	@Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

	// negative
	@Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

	// positive
	@Test
    void testLoginUser_Success() {
        when(userRepository.findByUserNameAndPassword("abhishek", "123")).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO loggedInUser = userService.loginUser("abhishek", "123");

        assertNotNull(loggedInUser);
        assertEquals(userDTO.getEmail(), loggedInUser.getEmail());
    }

	// negative

	@Test
    void testLoginUser_InvalidCredentials() {
        when(userRepository.findByUserNameAndPassword("abhi", "wrongpassword")).thenReturn(null);

        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            userService.loginUser("abhi", "wrongpassword");
        });

        assertEquals("Invalid username or password", exception.getMessage());
    }
}
