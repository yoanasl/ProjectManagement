package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String password;
    private String name;
    @ManyToMany(mappedBy = "users")
    private List<Project> projects;
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
    @ManyToMany(mappedBy = "members")
    private List<Team> teams;

}
