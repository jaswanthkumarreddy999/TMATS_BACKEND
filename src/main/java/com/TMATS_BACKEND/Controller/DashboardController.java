package com.TMATS_BACKEND.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.TMATS_BACKEND.Model.Role;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@Controller
public class DashboardController {

    @GetMapping("/Dashboard")
    public String home(HttpSession session, Model model) {
        System.out.println("Dashboard controller accessed");
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/Home";
        }
        
        // Verify role
        Object roleObj = session.getAttribute("role");
        System.out.println("Role in Dashboard controller: " + roleObj + 
                           " (" + (roleObj != null ? roleObj.getClass().getName() : "null") + ")");
        
        // If user is an admin, redirect to Admin Dashboard
        if (roleObj != null) {
            if (roleObj instanceof Role && roleObj == Role.ADMIN) {
                System.out.println("Admin user accessing regular dashboard - redirecting to Admin Dashboard");
                return "redirect:/Admin/Dashboard";
            }
            
            if (roleObj.toString().equals("ADMIN")) {
                System.out.println("Admin user (string match) accessing regular dashboard - redirecting to Admin Dashboard");
                return "redirect:/Admin/Dashboard";
            }
        }
        
        // Add user information to the model
        model.addAttribute("email", session.getAttribute("user"));
        
        return "user-dashboard";
    }  
    
    @GetMapping("/Admin/Dashboard")
    public String greetadmin(HttpSession session, Model model) {
        System.out.println("Admin Dashboard controller accessed");
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            return "redirect:/Home";
        }
        
        // Verify admin role
        Object roleObj = session.getAttribute("role");
        System.out.println("Role in Admin Dashboard controller: " + roleObj + 
                           " (" + (roleObj != null ? roleObj.getClass().getName() : "null") + ")");
        
        boolean isAdmin = false;
        
        if (roleObj != null) {
            if (roleObj instanceof Role && roleObj == Role.ADMIN) {
                isAdmin = true;
            } else if (roleObj.toString().equals("ADMIN")) {
                isAdmin = true;
            }
        }
        
        if (!isAdmin) {
            return "redirect:/Dashboard";
        }
        
        // Add user information to the model
        model.addAttribute("email", session.getAttribute("user"));
        
        return "admin-dashboard";
    }
    
    // Updated lowercase admin dashboard route for consistency
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        return greetadmin(session, model);
    }
    
    // New admin routes for users, data, and crops
    @GetMapping("/admin/users")
    public String adminUsers(HttpSession session, Model model) {
        if (!validateAdminAccess(session)) {
            return "redirect:/Dashboard";
        }
        
        model.addAttribute("email", session.getAttribute("user"));
        return "admin-users";
    }
    
    @GetMapping("/admin/data")
    public String adminData(HttpSession session, Model model) {
        if (!validateAdminAccess(session)) {
            return "redirect:/Dashboard";
        }
        
        model.addAttribute("email", session.getAttribute("user"));
        return "admin-data";
    }
    
    @GetMapping("/admin/crops")
    public String adminCrops(HttpSession session, Model model) {
        if (!validateAdminAccess(session)) {
            return "redirect:/Dashboard";
        }
        
        model.addAttribute("email", session.getAttribute("user"));
        return "admin-crops";
    }
    
    // Helper method to validate admin access
    private boolean validateAdminAccess(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return false;
        }
        
        Object roleObj = session.getAttribute("role");
        
        if (roleObj != null) {
            if (roleObj instanceof Role && roleObj == Role.ADMIN) {
                return true;
            } else if (roleObj.toString().equals("ADMIN")) {
                return true;
            }
        }
        
        return false;
    }
    
    // Debug endpoint to print session info
    @GetMapping("/debug-session")
    @ResponseBody
    public String debugSession(HttpSession session) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Session Debug Info</h1>");
        
        if (session == null) {
            sb.append("<p>No session found</p>");
            return sb.toString();
        }
        
        sb.append("<h2>Session Attributes:</h2><ul>");
        java.util.Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object value = session.getAttribute(name);
            sb.append("<li>").append(name).append(" = ").append(value);
            if (value != null) {
                sb.append(" (").append(value.getClass().getName()).append(")");
                
                if (name.equals("role")) {
                    sb.append("<br/>toString(): ").append(value.toString());
                    sb.append("<br/>equals ADMIN: ").append(value.toString().equals("ADMIN"));
                    sb.append("<br/>equals USER: ").append(value.toString().equals("USER"));
                    
                    if (value instanceof Role) {
                        sb.append("<br/>Is Role enum: true");
                        sb.append("<br/>equals Role.ADMIN: ").append(value == Role.ADMIN);
                        sb.append("<br/>equals Role.USER: ").append(value == Role.USER);
                    }
                }
            }
            sb.append("</li>");
        }
        sb.append("</ul>");
        
        return sb.toString();
    }
    
    // Uppercase admin routes for backward compatibility
    @GetMapping("/Admin/users")
    public String adminUsersUppercase(HttpSession session, Model model) {
        return "redirect:/admin/users";
    }
    
    @GetMapping("/Admin/data")
    public String adminDataUppercase(HttpSession session, Model model) {
        return "redirect:/admin/data";
    }
    
    @GetMapping("/Admin/crops")
    public String adminCropsUppercase(HttpSession session, Model model) {
        return "redirect:/admin/crops";
    }
}
