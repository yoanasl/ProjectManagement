package com.example.demo.service;

import com.example.demo.config.CustomLogger;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl{

    private UserRepository userRepository;

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<User> findByProjectId(Long id){
      return   userRepository.findByProjectsId(id);
    }

    public void addComment(String userEmail, Comment comment) {
        User user = findByEmail(userEmail);
        user.getComments().add(comment);
        userRepository.save(user);
    }

    public List<Project> getProjects(String userEmail) {

        User user = findByEmail(userEmail);
        return user.getProjects();
    }

    public void addProjectToUser(String userEmail, Project project) {
        User user = findByEmail(userEmail);
        user.getProjects().add(project);
        userRepository.save(user);
    }

    public User getCurrentUserFromSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        return findByEmail(currentUserName);

    }


}
