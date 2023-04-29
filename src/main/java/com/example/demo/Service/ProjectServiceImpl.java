package com.example.demo.Service;

import com.example.demo.Entity.*;
import com.example.demo.Entity.ProjectDTO;
import com.example.demo.Entity.UserDTO;
import com.example.demo.exceptions.ProjectNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class ProjectServiceImpl{
    @Autowired
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;


    public ProjectDTO createProject(ProjectDTO projectDTO){
        // validate input data
        if(projectDTO.getName() == null || projectDTO.getName().isEmpty()){
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        if(isNameExist(projectDTO.getName())){
            throw new IllegalArgumentException("Project name already exist");
        }
        if(projectDTO.getStartDate().compareTo(projectDTO.getEndDate()) > 0){
            throw new IllegalArgumentException("Project start date cannot be after end date");
        }

        // create a new project entity and set its properties
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        // save the new project in the database and convert it to a DTO
        Project savedProject = projectRepository.save(project);
        return new ProjectDTO(savedProject.getId(), savedProject.getName(), savedProject.getDescription(),
                savedProject.getStartDate(), savedProject.getEndDate());
    }

    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO){
        if(projectDTO.getName() == null || projectDTO.getName().isEmpty()){
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if(projectDTO.getStartDate().compareTo(projectDTO.getEndDate()) > 0){
            throw new IllegalArgumentException("Project start date cannot be after end date");
        }
        // retrieve the project from the database
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(projectOptional.isEmpty()){
            throw new EntityNotFoundException("Project with ID " + id + " not found");
        }
        Project existingProject = projectOptional.get();
        // update the project entity and save it in the database
        existingProject.setName(projectDTO.getName());
        existingProject.setDescription(projectDTO.getDescription());
        existingProject.setStartDate(projectDTO.getStartDate());
        existingProject.setEndDate(projectDTO.getEndDate());
        Project updatedProject = projectRepository.save(existingProject);
        // convert the updated project to a DTO and return it
        return new ProjectDTO(updatedProject.getId(), updatedProject.getName(), updatedProject.getDescription(),
                updatedProject.getStartDate(), updatedProject.getEndDate());
    }


    public void deleteProject(Long id){
        // validate input data
        if(id == null){
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        // check if the project exists in the database
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(projectOptional.isEmpty()){
            throw new EntityNotFoundException("Project with ID " + id + " not found");
        }
        // delete the project from the database
        projectRepository.deleteById(id);
    }

    public List<ProjectDTO> findAllProjects(){
        // retrieve all projects from the database
        List<Project> projects = projectRepository.findAll();
        // convert the list of project entities to a list of project DTOs
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for(Project project : projects){
            ProjectDTO projectDTO = new ProjectDTO(project.getId(), project.getName(), project.getDescription(),
                    project.getStartDate(), project.getEndDate());
            projectDTOs.add(projectDTO);
        }
        return projectDTOs;
    }

    public void assignUserToProject(Long userId, Long projectId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        project.getUsers().add(user);
        projectRepository.save(project);
    }

    public void removeUserFromProject(Long userId, Long projectId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        project.getUsers().remove(user);
        projectRepository.save(project);
    }

    public List<UserDTO> getUsersByProjectId(Long projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        List<User> users = project.getUsers();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    private Boolean isNameExist(String name){
        return projectRepository.findByName(name).isPresent();
    }


//    public void assignTaskToProject(Long taskId, Long projectId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
//
//        task.setProject(project);
//        taskRepository.save(task);
//    }
//
//    public void removeTaskFromProject(Long taskId, Long projectId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
//
//        if (task.getProject() != null && task.getProject().equals(project)) {
//            task.setProject(null);
//            taskRepository.save(task);
//        }
//    }
}
