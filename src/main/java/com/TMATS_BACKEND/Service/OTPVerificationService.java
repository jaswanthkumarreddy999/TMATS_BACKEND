package com.TMATS_BACKEND.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TMATS_BACKEND.Model.User;
import com.TMATS_BACKEND.Model.VerificationOTP;
import com.TMATS_BACKEND.Repo.UserRepo;
import com.TMATS_BACKEND.Repo.VerificationOTPRepository;

@Service
public class OTPVerificationService {
    
    @Autowired
    private VerificationOTPRepository otpRepository;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * Generate OTP for a user and send via email
     */
    @Transactional
    public boolean generateAndSendOTP(User user) {
        System.out.println("\n[DEBUG] Generating OTP for user: " + user.getUsername());
        
        // Delete any existing OTPs for the user
        otpRepository.deleteByUser(user);
        
        // Generate a 6-digit OTP
        String otp = generateOTP();
        System.out.println("[DEBUG] Generated OTP: " + otp);
        
        // Create OTP entity
        VerificationOTP verificationOTP = new VerificationOTP();
        verificationOTP.setUser(user);
        verificationOTP.setOtp(otp);
        verificationOTP.setExpiryDate(LocalDateTime.now().plusMinutes(10)); // OTP valid for 10 minutes
        
        // Save OTP
        otpRepository.save(verificationOTP);
        
        // Send OTP via email
        return sendOTPEmail(user.getEmail(), otp);
    }
    
    /**
     * Verify OTP and mark user as verified
     */
    @Transactional
    public boolean verifyOTP(String otp) {
        System.out.println("\n[DEBUG] Verifying OTP: " + otp);
        
        Optional<VerificationOTP> otpOpt = otpRepository.findByOtp(otp);
        
        if (otpOpt.isEmpty()) {
            System.out.println("[DEBUG] OTP not found in database");
            return false;
        }
        
        VerificationOTP verificationOTP = otpOpt.get();
        
        // Check if OTP is expired
        if (verificationOTP.isExpired()) {
            System.out.println("[DEBUG] OTP has expired");
            return false;
        }
        
        // Mark user as verified
        User user = verificationOTP.getUser();
        user.setVerified(true);
        userRepo.save(user);
        
        // Delete the used OTP
        otpRepository.delete(verificationOTP);
        
        System.out.println("[DEBUG] OTP verified successfully for user: " + user.getUsername());
        return true;
    }
    
    /**
     * Regenerate OTP for an existing user
     */
    @Transactional
    public boolean regenerateOTP(String email) {
        System.out.println("\n[DEBUG] Regenerating OTP for email: " + email);
        
        Optional<User> userOpt = userRepo.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            System.out.println("[DEBUG] User not found with email: " + email);
            return false;
        }
        
        User user = userOpt.get();
        
        // Check if user is already verified
        if (user.isVerified()) {
            System.out.println("[DEBUG] User is already verified");
            return false;
        }
        
        // Generate new OTP
        return generateAndSendOTP(user);
    }
    
    /**
     * Generate a random 6-digit OTP
     */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit number
        return String.valueOf(otp);
    }
    
    /**
     * Send OTP via email
     */
    private boolean sendOTPEmail(String email, String otp) {
        try {
            // Log to console for debugging
            System.out.println("\n==================================================");
            System.out.println("SENDING OTP EMAIL:");
            System.out.println("OTP: " + otp);
            System.out.println("To: " + email);
            System.out.println("==================================================\n");
            
            // Create email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom("noreply@tmats.com");
            message.setSubject("Your TMATS Verification Code");
            message.setText("Hello,\n\nYour verification code is: " + otp + 
                    "\n\nThis code will expire in 10 minutes. Please use it to verify your account.\n\n" +
                    "If you did not request this code, please ignore this email.");
            
            // Send email
            mailSender.send(message);
            
            return true;
        } catch (Exception e) {
            System.out.println("\n==================================================");
            System.out.println("ERROR SENDING OTP EMAIL:");
            e.printStackTrace();
            System.out.println("==================================================\n");
            return false;
        }
    }
} 