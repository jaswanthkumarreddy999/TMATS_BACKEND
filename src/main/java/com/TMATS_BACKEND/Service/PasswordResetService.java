package com.TMATS_BACKEND.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TMATS_BACKEND.Model.PasswordResetToken;
import com.TMATS_BACKEND.Model.User;
import com.TMATS_BACKEND.Repo.PasswordResetTokenRepository;
import com.TMATS_BACKEND.Repo.UserRepo;

@Service
public class PasswordResetService {
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Send password reset email to the user
     */
    @Transactional
    public boolean sendPasswordResetEmail(String email) {
        System.out.println("\n[DEBUG] Attempting to send password reset for email: " + email);
        
        // Find user by email
        Optional<User> userOpt = userRepo.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            // User not found with the provided email
            System.out.println("[DEBUG] No user found with email: " + email);
            return false;
        }
        
        User user = userOpt.get();
        System.out.println("[DEBUG] User found: " + user.getUsername());
        
        // Generate a token
        String token = generateResetToken(user);
        System.out.println("[DEBUG] Token generated: " + token);
        
        // Send email
        return sendEmail(user.getEmail(), token);
    }
    
    /**
     * Validate the reset token
     */
    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        
        // Check if the token is expired
        return !isTokenExpired(resetToken);
    }
    
    /**
     * Reset the user's password
     */
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (tokenOpt.isEmpty() || isTokenExpired(tokenOpt.get())) {
            return false;
        }
        
        // Get the user from the token
        User user = tokenOpt.get().getUser();
        
        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        
        // Delete the used token
        tokenRepository.delete(tokenOpt.get());
        
        return true;
    }
    
    /**
     * Generate a unique reset token for the user
     */
    @Transactional
    private String generateResetToken(User user) {
        // Delete any existing tokens for the user
        tokenRepository.deleteByUser(user);
        
        // Create a new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
        
        tokenRepository.save(resetToken);
        
        return token;
    }
    
    /**
     * Send the password reset email
     */
    private boolean sendEmail(String email, String token) {
        try {
            // Create the reset URL
            String resetUrl = "http://localhost:8080/reset-password?token=" + token;
            
            // Still log to console for debugging purposes
            System.out.println("\n\n==================================================");
            System.out.println("PASSWORD RESET URL (SENDING TO EMAIL):");
            System.out.println(resetUrl);
            System.out.println("Sending to email: " + email);
            System.out.println("==================================================\n\n");
            
            // Create the email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom("noreply@tmats.com"); // Add a from address
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, click the link below:\n\n" + resetUrl
                    + "\n\nIf you did not request a password reset, please ignore this email.");
            
            // Send the email
            mailSender.send(message);
            
            return true;
        } catch (Exception e) {
            System.out.println("\n\n==================================================");
            System.out.println("ERROR SENDING EMAIL:");
            e.printStackTrace();
            System.out.println("==================================================\n\n");
            return false;
        }
    }
    
    /**
     * Check if the token is expired
     */
    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }
} 