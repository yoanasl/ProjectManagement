package com.example.demo.dto;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Project;
import com.example.demo.entity.Team;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{

    private String email;
    private String password;
    private String name;

    private List<ProjectDTO> projects;
    private List<Comment> comments;
    private List<Team> teams;

}
