package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{

    private String email;
    private String password;
    private String name;

    private List<Project> projects;
    private List<Comment> comments;
    private List<Team> teams;

}
