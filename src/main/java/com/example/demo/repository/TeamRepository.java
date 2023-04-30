package com.example.demo.repository;

import com.example.demo.entity.Project;
import com.example.demo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{

    Team findByProject(Project project);
}
