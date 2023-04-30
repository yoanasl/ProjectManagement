package com.example.demo.controller;

import com.example.demo.service.ProjectServiceImpl;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import com.example.demo.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/projects")
public class ProjectController{
    @Autowired
    private ProjectServiceImpl projectService;

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
