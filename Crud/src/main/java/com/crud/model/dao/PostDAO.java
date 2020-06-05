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

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @NotNull
    private Boolean isDrafted;

    private Set<Tag> existingTags = new HashSet<>();

    private Set<Tag> newTags = new HashSet<>();

    private String[] authorsId;

    public PostDAO(String title,String body,Boolean isDrafted){
        this.title = title;
        this.body = body;
        this.isDrafted = isDrafted;
    }

    public PostDAO(String title,String body,User user,Date date){
        this.title = title;
        this.body = body;
    }
}
