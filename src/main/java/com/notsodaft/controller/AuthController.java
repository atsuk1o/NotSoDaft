package com.notsodaft.controller;

import com.notsodaft.model.PasswordResetToken;
import com.notsodaft.model.User;
import com.notsodaft.service.PasswordResetService;
import com.notsodaft.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController{
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    public AuthController(UserService userService, PasswordResetService passwordResetService){
        this.userService = userService;
        this.passwordResetService = passwordResetService;
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
                        Model model){
        try{
            userService.register(name, email, password, User.Role.TENANT);
            return "redirect:/login?registered";
        }catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(){
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, HttpServletRequest request, RedirectAttributes redirectAttributes){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        passwordResetService.sendResetEmail(email, baseUrl);
        redirectAttributes.addFlashAttribute("success", "If that email is registered, a reset link has been sent.");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model){
        PasswordResetToken resetToken = passwordResetService.findByToken(token);
        if (resetToken == null || resetToken.isExpired()){
            model.addAttribute("error", "Reset link is invalid or has expired.");
            return "auth/reset-password";
        }
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes){
        if (!password.equals(confirmPassword)){
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?token=" + token;
        }
        if (password.length() < 8){
            redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters.");
            return "redirect:/reset-password?token=" + token;
        }
        boolean success = passwordResetService.resetPassword(token, password);
        if (success){
            redirectAttributes.addFlashAttribute("success", "Password reset successfully. Please log in.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Reset link is invalid or has expired.");
            return "redirect:/reset-password?token=" + token;
        }
    }
}