package com.crud.model.dao;

import com.crud.model.Tag;
import com.crud.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDAO {

    private Long id;

    private String title;


    private String body;


    private Date publishDate;


    private Boolean isPublished;

    private Boolean isDrafted;

    private Set<Tag> tags = new HashSet<>();

    private Set<Tag> alternateTags = new HashSet<>();

    public PostDAO(String title,String body,User user,Date date){
        this.title = title;
        this.body = body;
    }

    public PostDAO(Long id,String title,String body,User user,Date date){
        this.id = id;
        this.title = title;
        this.body = body;

    }
}
