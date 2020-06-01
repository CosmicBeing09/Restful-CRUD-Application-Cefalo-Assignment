package com.crud.controller;

import com.crud.jwt.JwtTokenUtil;
import com.crud.model.Post;
import com.crud.model.User;
import com.crud.model.dao.PostDAO;
import com.crud.service.PostService;
import com.crud.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    public String token;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    UserService userService;


    @Before
    public void setUp() throws ServletException, IOException {
        User user = new User("raihan123", "raihan", "rai123");
        Post post1 = new Post(-3000L, "Title 1", "Body 1", user, new Date());
        Post post2 = new Post(-4000L, "Title 2", "Body 2", user, new Date());
        Post post3 = new Post(-5000L, "Title 3", "Body 3", user, new Date());

        List<Post> listPost = new ArrayList<>();
        listPost.add(post1);
        listPost.add(post2);
        listPost.add(post3);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), new ArrayList<>());
        token = JwtTokenUtil.generateToken(userDetails);

        Mockito.when(userService.loadUserByUsername(any(String.class))).thenReturn(userDetails);
        Mockito.when(postService.createPost(any(String.class), any(PostDAO.class))).thenReturn(true);
        Mockito.when(postService.retrieveAllPost(any(Integer.class),any(Integer.class))).thenReturn(listPost);
        Mockito.when(postService.searchPost(any(String.class),any(Integer.class),any(Integer.class))).thenReturn(listPost);
        Mockito.when(postService.retrievePostById(any(Long.class))).thenReturn(Optional.empty());
        Mockito.when(postService.updatePost(any(Post.class),any(UserDetails.class))).thenReturn(true);
        Mockito.when(postService.retrieveAllPostByUserId(any(String.class))).thenReturn(listPost);
        Mockito.when(postService.deletePost(any(Long.class),any(UserDetails.class))).thenReturn(false);
        Mockito.when(postService.totalDataSize()).thenReturn(listPost.size());
    }

    @Test
    public void createPost() throws Exception {

        PostDAO post = new PostDAO("Title","Body",false);

        Gson gson = new Gson();
        String json = gson.toJson(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/raihan123").header("Authorization","Bearer "+token)
        .contentType(MediaType.APPLICATION_JSON)
                .content(json))
         .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void retrieveAllPost() throws Exception {
       MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/posts?pageNo=1&pageSize=5").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Post> temp = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Post>>() {});

        assertEquals(temp.size(),3);
    }


    @Test
    public void searchPost() throws Exception {
        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/posts/search?pageNo=1&pageSize=5&text=title").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Post> temp = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Post>>() {});

        assertEquals(temp.size(),3);
    }

    @Test
    public void retrievePostById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/post/123").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updatePost() throws Exception {

        User user = new User("raihan123","raihan","rai123");
        Post post = new Post("Title","Body",user,new Date(),false);

        Gson gson = new Gson();
        String json = gson.toJson(post);

        mockMvc.perform(MockMvcRequestBuilders.put("/post").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void retrieveAllPostByUserId() throws Exception {
        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/user-posts/raihan123").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<Post> temp = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Post>>() {});

        assertEquals(temp.size(),3);
    }

    @Test
    public void deletePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/123").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void totalDataSize() throws Exception {
        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/posts/size").header("Authorization","Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Integer size = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Integer>() {});

        assertEquals(size,3);
    }
}