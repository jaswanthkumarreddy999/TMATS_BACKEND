package com.TMATS_BACKEND.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.TMATS_BACKEND.DTO.LoginDTO;
import com.TMATS_BACKEND.DTO.RegisterDTO;
import com.TMATS_BACKEND.Model.Role;
import com.TMATS_BACKEND.Model.User;
import com.TMATS_BACKEND.Repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo ur;
    
    @Autowired
    private OTPVerificationService otpService;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDTO registerDTO) {
        // Check if user already exists
        if (ur.findByEmail(registerDTO.getEmail()).isPresent()) {
            return null; // User already exists
        }
        
        // Create new user
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setUsername(registerDTO.getUsername());
        user.setRole(Role.USER);
        user.setVerified(false); // Set as unverified initially
        
        // Save user
        User savedUser = ur.save(user);
        
        // Generate and send OTP
        otpService.generateAndSendOTP(savedUser);
        
        return savedUser;
    }

    public int login(LoginDTO loginDTO) {
        Optional<User> userOpt = ur.findByEmail(loginDTO.getEmail());
        
        if(userOpt.isEmpty()) return 1;
        
        User user = userOpt.get();
        
        // Check if user is verified
        if (!user.isVerified()) {
            return 3; // User not verified
        }
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) return 2;
        
        return 0;
    }

    public Role getUserRole(String email) {
        Optional<User> user = ur.findByEmail(email);
        return user.get().getRole();
    }
    
    /**
     * Resend OTP verification code to user
     */
    public boolean resendOTP(String email) {
        return otpService.regenerateOTP(email);
    }
}
