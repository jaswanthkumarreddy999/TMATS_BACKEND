package com.TMATS_BACKEND.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TMATS_BACKEND.DTO.ForgotPasswordDTO;
import com.TMATS_BACKEND.DTO.ResetPasswordDTO;
import com.TMATS_BACKEND.Service.PasswordResetService;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;
    
    /**
     * Display the forgot password page
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }
    
    /**
     * Display the reset password page
     */
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        // Validate token before showing the reset page
        if (passwordResetService.validateResetToken(token)) {
            model.addAttribute("token", token);
            return "reset-password";
        }
        
        // If token is invalid, redirect to error page or forgot password with error
        return "redirect:/forgot-password?error=invalid_token";
    }
    
    /**
     * Rest controller for API calls related to password reset
     */
    @RestController
    public static class PasswordResetApiController {
        
        @Autowired
        private PasswordResetService passwordResetService;
        
        /**
         * Handle the forgot password form submission
         */
        @PostMapping("/api/auth/forgot-password")
        public ResponseEntity<?> processForgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
            boolean emailSent = passwordResetService.sendPasswordResetEmail(forgotPasswordDTO.getEmail());
            
            if (emailSent) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", "Failed to send reset email. Please try again.")
                );
            }
        }
        
        /**
         * Handle the reset password form submission
         */
        @PostMapping("/api/auth/reset-password")
        public ResponseEntity<?> processResetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
            boolean resetSuccessful = passwordResetService.resetPassword(
                resetPasswordDTO.getToken(), 
                resetPasswordDTO.getPassword()
            );
            
            if (resetSuccessful) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", "Failed to reset password. Token may be invalid or expired.")
                );
            }
        }
    }
} 