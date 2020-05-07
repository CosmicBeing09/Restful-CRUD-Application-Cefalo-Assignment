package com.crud.repository;

import com.crud.model.Post;
import com.crud.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag,Long> {
    @Query("select t from Tag t")
    List<Tag> findAll();
}
