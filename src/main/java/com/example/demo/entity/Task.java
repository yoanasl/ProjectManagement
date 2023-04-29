package com.example.demo.entity;

import com.example.demo.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String description;
    private int priority;
    private Status status;
    @NonNull
    private String startDate;
    @NonNull
    private String endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Task(String name, String description, int priority,
                Status status, @NonNull String startDate,
                @NonNull String endDate, Project project, User user) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
        this.user = user;
    }
}
