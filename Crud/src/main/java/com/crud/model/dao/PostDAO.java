package com.crud.model.dao;

import com.crud.model.Tag;
import com.crud.model.User;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDAO {

    private Long id;

    @Size(max = 250)
    @NotNull
    private String title;

    @Size(max = 10000)
    @NotNull
    private String body;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @NotNull
    private Boolean isPublished;

    @NotNull
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
