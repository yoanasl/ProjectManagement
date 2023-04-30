package com.example.demo.controller;

import com.example.demo.service.ProjectServiceImpl;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.service.ProjectServiceImpl;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import com.example.demo.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}/team")
    public String getTeamForProject(@PathVariable("id") Long projectId, Model model) {

        model.addAttribute("project", projectService.getProject(projectId));
        model.addAttribute("team", projectService.getUsersByProjectId(projectId));

        return "projectTeam";

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
    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    public String createProject(@ModelAttribute("project") ProjectDTO projectDTO, Model model){
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        model.addAttribute("project", createdProject);
        return "projectCreated";
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO){
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateProject/{id}", method = RequestMethod.POST)
    public String updateProject(@PathVariable("id") Long id, @ModelAttribute("project") ProjectDTO projectDTO, Model model){
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        model.addAttribute("project", updatedProject);
        model.addAttribute("message", "Project updated successfully.");
        return "projectDetails";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value = "/deleteProject/{id}", method = RequestMethod.POST)
    public String deleteProject(@PathVariable("id") Long id, Model model){
        projectService.deleteProject(id);
        model.addAttribute("message", "Project deleted successfully.");
        return "redirect:/projects";
    }
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(){
        List<ProjectDTO> projects = projectService.findAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String getAllProjects(Model model){
        List<ProjectDTO> projects = projectService.findAllProjects();
        model.addAttribute("projects", projects);
        return "projects";
    }

//    @GetMapping("/{projectId}")
//    public ResponseEntity<Team> getProjectAndTeamByProjectId(@PathVariable Long projectId) {
//        Team team = projectService.getProjectAndTeamByProjectId(projectId);
//        return ResponseEntity.ok(team);
//    }
//    @GetMapping("/{projectId}")
//    public Team getProjectAndTeamByProjectId(@PathVariable Long projectId) {
//        Team team = projectService.getProjectAndTeamByProjectId(projectId);
//        System.out.println(team);
//        return team;
//    }


}
