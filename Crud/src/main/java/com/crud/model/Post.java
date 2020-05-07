package com.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "post")
@Table(name = "post")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JacksonXmlRootElement(localName = "post")
public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private Long id;

    @Size(max = 250)
    @NotNull
    private String title;

    @Size(max = 10000)
    @NotNull
    private String body;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
    @JsonManagedReference @JsonIgnore
    private User user;

    private Boolean isPublished;

    private Boolean isDrafted;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    public Post(Long id,String title,String body){
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public Post(String title,String body,User user,Date date){
        this.title = title;
        this.body = body;
        this.user = user;
        this.date = date;
    }

    public Post(Long id,String title,String body,User user,Date date){
        this.id = id;
        this.title = title;
        this.body = body;
        this.user = user;
        this.date = date;
    }
}
