package com.notsodaft.controller;

import com.notsodaft.model.User;
import com.notsodaft.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController{
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("roles", User.Role.values());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam User.Role role,
                           Model model) {
        try{
            userService.register(name, email, password, role);
            return "redirect:/login?registered";
        }catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}