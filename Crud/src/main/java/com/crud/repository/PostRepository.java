package com.crud.repository;

import com.crud.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post,Long> {

    @Query(value = "select p from post p where user_id = :userId ")
    List<Post> findAllPostByUserId(String userId);
}