package com.example.demo.controller;

import com.example.demo.Service.RegisterService;
import com.example.demo.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController{
    private final RegisterService registerService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDto){
        if(registerService.registerUser(userDto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
