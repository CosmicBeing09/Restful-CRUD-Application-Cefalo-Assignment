package com.crud.service;

import com.crud.jwt.models.JwtRequest;
import com.crud.model.User;
import com.crud.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    AuthenticationManager authenticationManager;



    @Before
    public void setUp(){
        User user = new User("raihan123","raihan","rai123");
        Mockito.when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());
        Mockito.when(userRepository.getOne(any(String.class))).thenReturn(user);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
    }

    @Test
    public void createUser() {
        User user = new User("raihan123","raihan","rai123");
        assertTrue(userService.createUser(user));
    }

    @Test
    public void authenticate() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("raihan123","rai123");
        assertFalse(userService.authenticate(jwtRequest).isEmpty());
    }

    @Test
    public void loadUserByUsername() {
        assertEquals(userService.loadUserByUsername("raihan123").getUsername(),"raihan123");
    }
}