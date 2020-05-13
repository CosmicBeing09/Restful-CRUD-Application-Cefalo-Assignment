package com.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tag")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tag{

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();
}
