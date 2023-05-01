package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService{

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


    public User createUser(UserDTO userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        return userRepository.save(user);
    }
    public UserDTO updateUser(UserDTO user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (!existingUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User updatedUser = existingUser.get();
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setName(user.getName());
        updatedUser = userRepository.save(updatedUser);
        return new UserDTO(updatedUser);
    }


    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (!existingUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        userRepository.delete(existingUser.get());
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


}
