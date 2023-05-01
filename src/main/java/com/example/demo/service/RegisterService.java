package com.example.demo.service;

import com.example.demo.config.CustomLogger;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exceptions.UserCannotBeCreatedException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.demo.enums.UserRole.*;

@Service
public class RegisterService{
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public RegisterService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public User registerUser(UserDTO userDto){
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new UserCannotBeCreatedException(userDto.getEmail());
        }
        String encodedPass = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPass);
        User userToCreate = new User();
        userToCreate.setPassword(userDto.getPassword());
        userToCreate.setName(userDto.getName());
        userToCreate.setEmail(userDto.getEmail());
        userToCreate.setGrantedAuthorities(ADMIN.getGrantedAuthority());
       try{
           userRepository.save(userToCreate);
       }catch(RuntimeException e)
       {
           throw new UserCannotBeCreatedException(userDto.getEmail());
       }
        return userToCreate;
    }

    /*@PostConstruct
    public void registerAdmin(){
        if(userRepository.existsByEmail("admin@abv.bg")){
            return;
        }
        User userToCreate = new User();
        //userToCreate.setId(1L);
        userToCreate.setPassword(passwordEncoder.encode("123456"));
        userToCreate.setEmail("admin@abv.bg");
        userToCreate.setGrantedAuthorities(ADMIN.getGrantedAuthority());
        userRepository.save(userToCreate);
    }

    @PostConstruct
    public void registerProjectManager(){
        if(userRepository.existsByEmail("projectManager@abv.bg")){
            return;
        }
        User userToCreate = new User();
        //userToCreate.setId(1L);
        userToCreate.setPassword(passwordEncoder.encode("123456"));
        userToCreate.setEmail("projectManager@abv.bg");
        userToCreate.setGrantedAuthorities(PROJECT_MANAGER.getGrantedAuthority());
        userRepository.save(userToCreate);
    }*/

}
