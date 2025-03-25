package com.TMATS_BACKEND.Controller;

import com.TMATS_BACKEND.DTO.LoginDTO;
import com.TMATS_BACKEND.DTO.RegisterDTO;
import com.TMATS_BACKEND.Model.Role;
import com.TMATS_BACKEND.Model.User;
import com.TMATS_BACKEND.Service.OTPVerificationService;
import com.TMATS_BACKEND.Service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
public class UserController {

    private final UserService us;
    private final OTPVerificationService otpService;

    @Autowired
    public UserController(UserService us, OTPVerificationService otpService) {
        this.us = us;
        this.otpService = otpService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/Home";
    }

    @GetMapping("/Home")
    public String home(Model model) {
        model.addAttribute("RegisterDTO", new RegisterDTO());
        model.addAttribute("LoginDTO", new LoginDTO());
        return "home";
    }
    @GetMapping("/Services")
    public String services(Model model) {
        model.addAttribute("RegisterDTO", new RegisterDTO());
        model.addAttribute("LoginDTO", new LoginDTO());
        return "services";
    }
    @GetMapping("/Products")
    public String products(Model model) {
        model.addAttribute("RegisterDTO", new RegisterDTO());
        model.addAttribute("LoginDTO", new LoginDTO());
        return "products";
    }
    @GetMapping("/Community")
    public String community(Model model) {
        model.addAttribute("RegisterDTO", new RegisterDTO());
        model.addAttribute("LoginDTO", new LoginDTO());
        return "community";
    }
    @GetMapping("/About")
    public String about(Model model) {
        model.addAttribute("RegisterDTO", new RegisterDTO());
        model.addAttribute("LoginDTO", new LoginDTO());
        return "about";
    }
    @GetMapping("/Contact")
    public String contact(Model model) {
        model.addAttribute("RegisterDTO", new RegisterDTO());
        model.addAttribute("LoginDTO", new LoginDTO());
        return "contact";
    }
    
    @GetMapping("/knowledge")
    public String knowledge(Model model, HttpSession session) {
        // Add any user information from session if available
        if (session.getAttribute("user") != null) {
            model.addAttribute("email", session.getAttribute("user"));
        } else {
            model.addAttribute("email", "Guest User");
        }
        // For now, redirect to community page until a knowledge page is created
        return "redirect:/Community";
    }
    
    /**
     * Display the verify OTP page
     */
    @GetMapping("/verify-otp")
    public String showVerifyOtpPage() {
        return "verify-otp";
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/Home";
    }
    
    @PostMapping("/Register")
    public String Register(@ModelAttribute("RegisterDTO") RegisterDTO registerDTO) {
        User user = us.register(registerDTO);
        if (user == null) {
            // User already exists
            return "user-exists";
        }
        return "redirect:/verify-otp?email=" + user.getEmail();
    }

    @PostMapping("/Login")
    public String Login(@ModelAttribute("LoginDTO") LoginDTO loginDTO, HttpSession session) {
        System.out.println(session.getAttribute("role"));
        int opt = us.login(loginDTO);
        if (opt == 0) {
            Role userRole = us.getUserRole(loginDTO.getEmail());
            session.setAttribute("user", loginDTO.getEmail());
            session.setAttribute("role", userRole);
            if(userRole == Role.ADMIN) {
                return "redirect:/Admin/Dashboard";
            }
            return "redirect:/Dashboard";
        } else if (opt == 1) {
            return "redirect:/UserNotFound";
        } else if (opt == 2) {
            return "redirect:/Wrongpassword";
        } else if (opt == 3) {
            // User not verified
            return "redirect:/UserNotVerified?email=" + loginDTO.getEmail();
        }
        return "redirect:/Home";
    }
    
    /**
     * Rest controller for API calls related to user management
     */
    @RestController
    @RequestMapping("/api/auth")
    public static class UserApiController {
        
        private final UserService userService;
        private final OTPVerificationService otpService;
        
        @Autowired
        public UserApiController(UserService userService, OTPVerificationService otpService) {
            this.userService = userService;
            this.otpService = otpService;
        }
        
        /**
         * Verify OTP code
         */
        @PostMapping("/verify-otp")
        public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
            String otp = payload.get("otp");
            
            boolean verified = otpService.verifyOTP(otp);
            
            if (verified) {
                return ResponseEntity.ok().build();
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Invalid or expired OTP code");
                return ResponseEntity.badRequest().body(response);
            }
        }
        
        /**
         * Resend OTP code
         */
        @PostMapping("/resend-otp")
        public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> payload) {
            String email = payload.get("email");
            
            boolean sent = userService.resendOTP(email);
            
            if (sent) {
                return ResponseEntity.ok().build();
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Failed to resend OTP");
                return ResponseEntity.badRequest().body(response);
            }
        }
    }
}    
