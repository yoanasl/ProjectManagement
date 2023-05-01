package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @OneToOne(mappedBy= "project")
    private Team team; //TODO: delete later

    public Project(String name, String description, @NonNull String startDate, @NonNull String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", users=" + users.stream().map(user -> user.getName()) +
                ", tasks=" + tasks.stream().map(task -> task.getName()) +
                '}';
    }
}
