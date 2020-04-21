package com.crud.repository;

import com.crud.model.Post;
import com.crud.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public  class PostRepositoryTest{

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    PostRepository postRepository;


    public PostRepositoryTest(){
    }


   @Before
   public void setUp(){
       User user = new User("raihan123","raihan","rai123");
       Post post = new Post("Test title","Test Body",user,new Date());
       Post post1 = new Post("Test title 1","Test Body 1",user,new Date());
       Post post2 = new Post("diff","diff",user,new Date());

       entityManager.persist(user);
       entityManager.persist(post);
       entityManager.persist(post1);
       entityManager.persist(post2);
       entityManager.flush();
   }

    @Test
    public void testFindAllPostByUserId() {
        assertEquals(postRepository.findAllPostByUserId("raihan123").size(),3);
    }

    @Test
    public void testSearchPost() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("date").descending());
        assertEquals(2,postRepository.searchPost("title",pageable).size());
    }

    @Test
    public void testGetOneById() {
        User user = new User("nafis123","nafis","rai123");
        Post post = new Post("Hello","Hi",user,new Date());

        entityManager.persist(user);
        entityManager.persist(post);
        entityManager.flush();

        Post found = postRepository.getOneById(post.getId());
        assertEquals(found.getTitle(),"Hello");
    }
}