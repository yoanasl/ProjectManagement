package com.example.demo.controller;

import com.example.demo.Service.RegisterService;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.security.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class UserController{
    private final RegisterService registerService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    @RequestMapping("/register")
    public String index(){
        return "registerView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute UserDTO userDto, Model model){

        if(validate(userDto)){
            registerService.registerUser(userDto);

            // Get the ID of the newly registered user
г
            // Redirect to the /get/{id} endpoint
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("projects/get/" + id);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView(redirectView);

            return modelAndView;
        } else{
            String errorMessage = "Incorrect credentials.";
            model.addAttribute("errorMessage", errorMessage);

            ModelAndView modelAndView = new ModelAndView("registerView");
            return modelAndView;
        }
    }

    private static boolean validate(UserDTO userDto){
        return userDto != null &&
                userDto.getPassword().equals(userDto.getPassword2()) &&
                userDto.getPassword().length() > 5;
    }

    @GetMapping("/login")
    public String login(){
        return "loginView";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String email, @RequestParam String password, Model model){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserDetails userDetails = userService.loadUserByUsername(email);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
            Long id = 2L; //TODO userDto.getId();

            // Redirect to the /get/{id} endpoint
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("projects/get/" + id);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView(redirectView);

            return modelAndView;
        } catch(BadCredentialsException e) {
            String errorMessage = "Incorrect credentials.";
            model.addAttribute("errorMessage", errorMessage);

            ModelAndView modelAndView = new ModelAndView("loginView");
            return modelAndView;
        }
    }


}
