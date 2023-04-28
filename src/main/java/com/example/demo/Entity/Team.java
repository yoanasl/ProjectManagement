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
public class Team{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(name = "team_user",
    joinColumns = @JoinColumn(name="team_id"),
    inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> members;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
