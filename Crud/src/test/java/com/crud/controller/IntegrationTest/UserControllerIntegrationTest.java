package com.crud.controller.IntegrationTest;

import com.crud.jwt.models.JwtRequest;
import com.crud.model.User;
import com.crud.repository.UserRepository;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integration.properties")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
    }

    @Test
    public void register() throws Exception {
        User user = new User("raihan1234", "raihan", "rai123");
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
        JwtRequest user = new JwtRequest("raihan737", "abc");
        Gson gson = new Gson();
        String json = gson.toJson(user);

        User createUser = new User("raihan737", "raihan", "abc");
        String registrationBody = gson.toJson(createUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_XML)
                .content(registrationBody))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    }
}