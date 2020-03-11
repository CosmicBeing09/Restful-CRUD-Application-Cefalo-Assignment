package com.crud.controller;

import com.crud.CrudApplication;
import com.crud.config.WebSecurityConfig;
import com.crud.model.Post;
import com.crud.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
//@SpringBootTest(classes = CrudApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest{


    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @Autowired
    private WebApplicationContext context;


    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void createPost() {
    }

    @Test
    public void retrieveAllPost() throws Exception {
        List<Post> list = new ArrayList<>();
        list.add(new Post((long) 1,"T1","B1"));
        list.add(new Post((long) 2,"T2","B2"));
        list.add(new Post((long) 3,"T3","B3"));

        BDDMockito.given(postService.retrieveAllPost(0,2))
                .willReturn(list);

        mvc.perform(MockMvcRequestBuilders.get("/posts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void retrievePostById() {
    }

    @Test
    public void updatePost() {
    }

    @Test
    public void retrieveAllPostByUserId() {
    }

    @Test
    public void deletePost() {
    }
}