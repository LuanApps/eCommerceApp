package com.example.ecommerce.controller;

import com.example.ecommerce.TestUtils;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.requests.CreateUserRequest;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        // Create a user
        User user = new User();
        user.setId(1L);
        user.setUsername("userTest");
        user.setPassword("userPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("userTest")).thenReturn(user);
    }

    @Test
    public void createUserHappyPath() throws Exception{
        when(encoder.encode("passwordTest")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("userTest");
        request.setPassword("passwordTest");
        request.setConfirmPassword("passwordTest");

        final ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("userTest", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void getUserByIdTest() throws Exception {

        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("userTest", user.getUsername());
    }

    @Test
    public void getUserByIdWhenUserNotExists(){
        final ResponseEntity<User> response = userController.findById(2L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getUserByUsernameTest(){
        final ResponseEntity<User> response = userController.findByUserName("userTest");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("userTest", user.getUsername());
        assertEquals(1L, user.getId());
    }

    @Test
    public void getUserByUsernameWhenUserNotExists(){
        final ResponseEntity<User> response = userController.findByUserName("otherUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
