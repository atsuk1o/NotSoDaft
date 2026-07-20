package com.notsodaft.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notsodaft.model.PasswordResetToken;
import com.notsodaft.model.User;
import com.notsodaft.repository.PasswordResetTokenRepository;
import com.notsodaft.repository.UserRepository;

@Service
public class PasswordResetService{
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository, UserRepository userRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder){
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void sendResetEmail(String email, String baseUrl){
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return; 

        tokenRepository.deleteByUser_Id(user.getId());

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);

        String resetUrl = baseUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test.yk@yahoo.com");
        message.setTo(email);
        message.setSubject("NotSoDaft - Password Reset");
        message.setText("Hello " + user.getName() + ",\n\n" + "Click the link below to reset your password. This link expires in 1 hour.\n\n" + resetUrl + "\n\n" + "If you did not request this, please ignore this email.\n\n" + "NotSoDaft Team");
        mailSender.send(message);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword){
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        if (resetToken == null || resetToken.isExpired()) return false;

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
        return true;
    }

    public PasswordResetToken findByToken(String token){
        return tokenRepository.findByToken(token).orElse(null);
    }
}