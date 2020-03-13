package com.crud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

//@OneToMany(mappedBy = "user")
//@JsonBackReference
//@JsonIgnore
//private List<Post> posts;


}
