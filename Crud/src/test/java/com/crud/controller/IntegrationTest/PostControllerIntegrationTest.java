package com.crud.controller.IntegrationTest;


import com.crud.controller.helperModel.TokenHelper;
import com.crud.jwt.models.JwtRequest;
import com.crud.model.Post;
import com.crud.model.User;
import com.crud.repository.PostRepository;
import com.crud.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class PostControllerIntegrationTest {

    public String token;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Before
    public void setUp() throws Exception {

        User user = new User("raihan123", "raihan", "rai123");
        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_XML)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        JwtRequest jwtRequest = new JwtRequest("raihan123", "rai123");
        String jsonBody = gson.toJson(jwtRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/authenticate")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String jsonResponse =  result.getResponse().getContentAsString();
        Gson gson1 = new GsonBuilder().create();
        TokenHelper jwtResponse = gson1.fromJson(jsonResponse,TokenHelper.class);
        token = jwtResponse.getToken();
    }

    @Test
    public void createPost() throws Exception {
        Post post = new Post(null,"Title","Body");

        Gson gson = new Gson();
        String json = gson.toJson(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/raihan123").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}