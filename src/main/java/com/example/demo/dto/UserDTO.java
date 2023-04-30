package com.example.demo.dto;

import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO{

    private String email;
    private String password;
    private String name;

    private List<ProjectDTO> projects;
    private List<Comment> comments;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
    }

}
