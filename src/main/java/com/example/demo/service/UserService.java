package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService{

    private UserRepository userRepository;

    public User createUser(UserDTO userDto) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
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
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
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


}
