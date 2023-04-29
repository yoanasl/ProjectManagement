package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectDTO{
    private String name;
    private String description;
    @NonNull
    private String startDate;
    @NonNull
    private String endDate;

    private List<User> users;

    private List<Task> tasks;

    @Transient
    private Team team;


    public ProjectDTO(Long id, String name, String description, @NonNull String startDate, @NonNull String endDate){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
