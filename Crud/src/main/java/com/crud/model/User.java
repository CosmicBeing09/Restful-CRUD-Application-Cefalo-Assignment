package com.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User{

    @Id
    @Column(name = "user_id")
    private String userId;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors")
    private Set<Post> authorsPosts = new HashSet<>();

    public User(String userId,String name,String password){
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

}
