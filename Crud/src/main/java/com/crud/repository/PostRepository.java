package com.crud.repository;

import com.crud.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post,Long> {

    @Query(value = "select p from post p where user_id = :userId ")
    List<Post> findAllPostByUserId(String userId);

//    @Query("select p from post p order by p.date")
//    List<Post> findAll();
}