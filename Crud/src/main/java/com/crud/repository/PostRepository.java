package com.crud.repository;

import com.crud.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post,Long> {

    @Query(value = "select p from post p where user_id = :userId")
    List<Post> findAllPostByUserId(String userId);

    @Query(value = "select p from post p where ( p.title like %:pattern%  or p.body like %:pattern% ) and isPublished = 1 and isDrafted = 0")
    List<Post> searchPost(String pattern, Pageable pageable);

    @Query(value = "select p from post  p where p.id = :id")
    Post getOneById(Long id);


    @Query("select p from post p order by p.date")
    List<Post> findAll();

    @Query("select p from post p where isPublished = 1 and isDrafted = 0")
    List<Post> findAllByPage(Pageable pageable);
}