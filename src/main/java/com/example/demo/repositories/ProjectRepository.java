package com.example.demo.repositories;

import com.example.demo.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project , Long>{
    Optional<Project> findByName(String name);
}
