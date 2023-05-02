package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String password;
    private String name;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_project",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="project_id"))
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_team",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="team_id"))
    private List<Team> teams;

    @OneToMany(mappedBy = "user")
    private Set<Task> tasks = new HashSet<>();

    private Set<? extends GrantedAuthority> grantedAuthorities;

    public User(Long id, String email, String password, String name){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", projects=" + projects.stream().map(Project::getId) +
                ", comments=" + comments.stream().map(Comment::getId) +
                ", tasks=" + tasks.stream().map(Task::getId) +
                '}';
    }
}
