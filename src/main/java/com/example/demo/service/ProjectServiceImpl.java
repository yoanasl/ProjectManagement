package com.example.demo.service;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ProjectNotFoundException;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class ProjectServiceImpl{

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    private final TeamRepository teamRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, ModelMapper modelMapper, TaskRepository taskRepository,TeamRepository teamRepository){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;
    }


    public Project getProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        return project.get();
    }
    public ProjectDTO createProject(ProjectDTO projectDTO){
        if(projectDTO.getName() == null || projectDTO.getName().isEmpty()){
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        if(isNameExist(projectDTO.getName())){
            throw new IllegalArgumentException("Project name already exist");
        }
        if(projectDTO.getStartDate().compareTo(projectDTO.getEndDate()) > 0){
            throw new IllegalArgumentException("Project start date cannot be after end date");
        }

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
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
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(projectOptional.isEmpty()){
            throw new EntityNotFoundException("Project with ID " + id + " not found");
        }
        Project existingProject = projectOptional.get();
        existingProject.setName(projectDTO.getName());
        existingProject.setDescription(projectDTO.getDescription());
        existingProject.setStartDate(projectDTO.getStartDate());
        existingProject.setEndDate(projectDTO.getEndDate());
        Project updatedProject = projectRepository.save(existingProject);
        return new ProjectDTO(updatedProject.getId(), updatedProject.getName(), updatedProject.getDescription(),
                updatedProject.getStartDate(), updatedProject.getEndDate());
    }


    public void deleteProject(Long id){
        if(id == null){
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(projectOptional.isEmpty()){
            throw new EntityNotFoundException("Project with ID " + id + " not found");
        }
        projectRepository.deleteById(id);
    }

    public List<ProjectDTO> findAllProjects(){
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for(Project project : projects){
            ProjectDTO projectDTO = new ProjectDTO(project.getId(), project.getName(), project.getDescription(),
                    project.getStartDate(), project.getEndDate());
            projectDTOs.add(projectDTO);
        }
        return projectDTOs;
    }

    public void assignUserToProject(Long userId, Long projectId){
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new UserNotFoundException(userId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.getUsers().add(user);
        projectRepository.save(project);
    }

    public void removeUserFromProject(Long userId, Long projectId){
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new UserNotFoundException(userId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.getUsers().remove(user);
        projectRepository.save(project);
    }

    public List<UserDTO> getUsersByProjectId(Long projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<User> users = project.getUsers();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    private Boolean isNameExist(String name){
        return projectRepository.findByName(name).isPresent();
    }

//    public Team getProjectAndTeamByProjectId(Long projectId){
//        Project project = projectRepository.findById(projectId).orElseThrow();
//
//        return teamRepository.findByProject(project);
//    }

//    public void assignTaskToProject(Long taskId, Long projectId){
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new TaskNotFoundException(taskId));
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ProjectNotFoundException(projectId));
//
//        task.setProject(project);
//        taskRepository.save(task);
//    }
//
//    public void removeTaskFromProject(Long taskId, Long projectId){
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new TaskNotFoundException(taskId));
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ProjectNotFoundException(projectId));
//
//        if(task.getProject() != null && task.getProject().equals(project)){
//            task.setProject(null);
//            taskRepository.save(task);
//        }
//    }
}
