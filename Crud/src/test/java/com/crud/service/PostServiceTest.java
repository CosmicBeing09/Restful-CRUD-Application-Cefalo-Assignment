package com.crud.service;

import com.crud.config.FileStorageProperties;
import com.crud.model.Post;
import com.crud.model.User;
import com.crud.repository.PostRepository;
import com.crud.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)

public class PostServiceTest {
    public PostServiceTest(){}

    @Autowired
    PostService postService;

    @MockBean
    PostRepository postRepository;

    @MockBean
    UserRepository userRepository;

    @Before
    public void setUp(){
        User user = new User("raihan123","raihan","rai123");
        Post post = new Post("Test title","Test Body",user,new Date());
        Post post1 = new Post("Test title 1","Test Body 1",user,new Date());
        Post post2 = new Post("diff","diff",user,new Date());

        List<Post> postList = new ArrayList<>();
        postList.add(post);
        postList.add(post1);
        postList.add(post2);

        final Page<Post> page = new PageImpl<>(postList);

        //Mockito.doReturn(page).when(postRepository).findAll(any(Pageable.class));
        Mockito.when(postRepository.findAll(any(Pageable.class))).thenReturn(page);
        Mockito.when(postRepository.findAll()).thenReturn(page);
        Mockito.when(postRepository.save(new Post())).thenReturn(post);

        //Optional<User> optUser = Optional.empty();
        Optional<User> optUser = Optional.of(user);
        Mockito.when(userRepository.findById(any(String.class))).thenReturn(optUser);

        Mockito.when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        Mockito.when(postRepository.searchPost(any(String.class),any(Pageable.class))).thenReturn(postList);
        Mockito.when(postRepository.findAllPostByUserId(any(String.class))).thenReturn(postList);
    }

    @Test
    public void createPost() {
        User user = new User("raihan123","raihan","rai123");
        Post post = new Post("Test title","Test Body",user,new Date());

        assertEquals(postService.createPost(user.getUserId(),post),true);
    }


    @Test
    public void retrievePostById() {
        Optional<Post> optUser = postService.retrievePostById(-2000L);
        assertTrue(optUser.isPresent());
    }

    @Test
    public void retrieveAllPost() {
        assertEquals(postService.retrieveAllPost(1,10).size(),3);
    }

    @Test
    public void searchPost() {
        assertFalse(postService.searchPost("search",1,5).isEmpty());
    }

    @Test
    public void retrieveAllPostByUserId() {
        assertFalse(postService.retrieveAllPostByUserId("raihan123").isEmpty());
    }

    @Test
    public void updatePost() {
        User user = new User("raihan123","raihan","rai123");
        Post post = new Post(-2000L,"Test title","Test Body",user,new Date());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserId(),user.getPassword(),new ArrayList<>());

        assertTrue(postService.updatePost(post,userDetails));
    }

    @Test
    public void deletePost() {
        User user = new User("raihan123","raihan","rai123");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserId(),user.getPassword(),new ArrayList<>());

        assertTrue(postService.deletePost(-2000L,userDetails));
    }

    @Test
    public void totalDataSize() {
        assertEquals(postService.totalDataSize(),3);
    }
}