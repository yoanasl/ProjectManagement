package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String description;
    private int priority;

    @ManyToOne
    @JoinColumn(name = "status_id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task(String name, String description, int priority,
                @NonNull String startDate, @NonNull String endDate) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
