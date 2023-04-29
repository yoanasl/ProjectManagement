package com.example.demo.controllers;

import com.example.demo.Entity.ProjectDTO;
import com.example.demo.Entity.UserDTO;
import com.example.demo.Service.ProjectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectServiceImpl projectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
       try{
           ProjectDTO createdProject = projectService.createProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);}
       catch(RuntimeException e){
           log.error(e.getMessage());
           return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
       }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.findAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> assignUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.assignUserToProject(userId, projectId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.removeUserFromProject(userId, projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<UserDTO>> getUsersByProjectId(@PathVariable Long projectId) {
        List<UserDTO> users = projectService.getUsersByProjectId(projectId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
