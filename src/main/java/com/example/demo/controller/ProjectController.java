package com.example.demo.controller;

import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.service.ProjectServiceImpl;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/projects")
public class ProjectController{

    private final ProjectServiceImpl projectService;

    public ProjectController(ProjectServiceImpl projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/get/{id}")
    public String getProject(@PathVariable("id") Long id, Model model){
        Project project = projectService.getProject(id);
        List<Task> toDoTasks = new ArrayList<>();
        List<Task> progressTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();
        for (Task task : project.getTasks()) {
            switch (task.getStatus().getId()) {
                case 1: toDoTasks.add(task); break;
                case 2: progressTasks.add(task); break;
                case 3: doneTasks.add(task); break;
            }
        }
        model.addAttribute("project", project);
        model.addAttribute("toDoTasks", toDoTasks);
        model.addAttribute("progressTasks", progressTasks);
        model.addAttribute("doneTasks", doneTasks);
        return "project";
    }


    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO){
        try{
            ProjectDTO createdProject = projectService.createProject(projectDTO);
            return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
        } catch(RuntimeException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO){
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(){
        List<ProjectDTO> projects = projectService.findAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> assignUserToProject(@PathVariable Long projectId, @PathVariable Long userId){
        projectService.assignUserToProject(userId, projectId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId){
        projectService.removeUserFromProject(userId, projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<UserDTO>> getUsersByProjectId(@PathVariable Long projectId){
        List<UserDTO> users = projectService.getUsersByProjectId(projectId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/{taskId}/assignToProject/{projectId}")
    public ResponseEntity<String> assignTaskToProject(@PathVariable Long taskId, @PathVariable Long projectId){
        projectService.assignTaskToProject(taskId, projectId);
        return ResponseEntity.ok("Task " + taskId + " has been assigned to project " + projectId);
    }

    @PostMapping("/{taskId}/removeFromProject/{projectId}")
    public ResponseEntity<String> removeTaskFromProject(@PathVariable Long taskId, @PathVariable Long projectId){
        projectService.removeTaskFromProject(taskId, projectId);
        return ResponseEntity.ok("Task " + taskId + " has been removed from project " + projectId);
    }
}
