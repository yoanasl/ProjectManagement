package com.example.demo.controller;

import com.example.demo.config.CustomLogger;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.security.UserService;
import com.example.demo.service.RegisterService;
import com.example.demo.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class UserController{
    private final RegisterService registerService;
    private final UserService userService;
    private final UserServiceImpl getUserServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;


    @RequestMapping("/register")
    public String index(){
        return "registerView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute UserDTO userDto, Model model){

        if(validate(userDto)){
            User user = registerService.registerUser(userDto);

            // Get the ID of the newly registered user
            Long id = user.getId();
            // Redirect to the /get/{id} endpoint
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("projects/" + id);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView(redirectView);

            return modelAndView;
        } else{
            String errorMessage = "Incorrect credentials.";
            model.addAttribute("errorMessage", errorMessage);

            ModelAndView modelAndView = new ModelAndView("registerView");
            CustomLogger.logInfo( " User submitted registration form with email " + userDto.getEmail());

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

            Long id = userServiceImpl.findByEmail(email).getId();

            // Redirect to the /get/{id} endpoint
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("projects/" + id);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView(redirectView);

            return modelAndView;
        } catch(BadCredentialsException e) {
            String errorMessage = "Incorrect credentials.";
            model.addAttribute("errorMessage", errorMessage);

            ModelAndView modelAndView = new ModelAndView("loginView");

            CustomLogger.logInfo("User attempted to log in with email " + email + " at " + LocalDateTime.now());

            return modelAndView;
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        CustomLogger.logInfo(userServiceImpl.getCurrentUserFromSession().getEmail()
                +": User logged out");


        return "redirect:/login";
    }


}
