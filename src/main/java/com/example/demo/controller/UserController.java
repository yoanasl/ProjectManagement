package com.example.demo.controller;

import com.example.demo.Service.RegisterService;
import com.example.demo.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController{
    private final RegisterService registerService;

//    public ResponseEntity<String> register(@RequestBody UserDTO userDto){
//        if(registerService.registerUser(userDto)){
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
@GetMapping
public String register(Model model) {
    model.addAttribute("user", "userDto");
    return "new";
}
}
