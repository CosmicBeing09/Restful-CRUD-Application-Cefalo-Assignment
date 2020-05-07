package com.crud.controller;

import com.crud.controller.helperModel.TokenHelper;
import com.crud.jwt.JwtTokenUtil;
import com.crud.jwt.models.JwtRequest;
import com.crud.model.User;
import com.crud.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        User user = new User("raihan123", "raihan", "rai123");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), new ArrayList<>());

        Mockito.when(userService.loadUserByUsername(any(String.class))).thenReturn(userDetails);
        Mockito.when(userService.createUser(any(User.class))).thenReturn(true);
        Mockito.when(userService.authenticate(any(JwtRequest.class))).thenReturn(JwtTokenUtil.generateToken(userDetails));
    }

    @Test
    public void register() throws Exception {
        User user = new User("raihan123", "raihan", "rai123");
        Gson gson = new Gson();
        String json = gson.toJson(user);

       MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_XML)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

       assertEquals(mvcResult.getResponse().getHeader("Content-Type"),MediaType.APPLICATION_XML_VALUE+";charset=UTF-8");
    }

    @Test
    public void authenticate() throws Exception {
        JwtRequest user = new JwtRequest("raihan123", "rai123");
        Gson gson = new Gson();
        String json = gson.toJson(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String jsonResponse =  result.getResponse().getContentAsString();
        System.out.println(result.getResponse().getContentAsString());
        Gson gson1 = new GsonBuilder().create();
        TokenHelper jwtResponse = gson1.fromJson(jsonResponse,TokenHelper.class);
        System.out.println(jwtResponse.getToken());
    }
}