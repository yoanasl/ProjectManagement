package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Project{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String description;
    @NonNull
    private String startDate;
    @NonNull
    private String endDate;

   @ManyToMany(mappedBy = "projects")
    private List<User> users;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @OneToOne(mappedBy= "project")
    private Team team;


}
