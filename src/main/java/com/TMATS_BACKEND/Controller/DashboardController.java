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
                return "redirect:/admin/dashboard";
            }
            
            if (roleObj.toString().equals("ADMIN")) {
                System.out.println("Admin user (string match) accessing regular dashboard - redirecting to Admin Dashboard");
                return "redirect:/admin/dashboard";
            }
        }
        
        // Add user information to the model
        model.addAttribute("email", session.getAttribute("user"));
        
        return "user-dashboard";
    }  
    
    // Simple admin dashboard - no redirection, no checks
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        System.out.println("Admin Dashboard controller accessed (lowercase) - direct");
        // Just render the template directly
        model.addAttribute("email", "admin@example.com");
        return "admin-dashboard";
    }
    
    // Uppercase version - simple redirect to lowercase
    @GetMapping("/Admin/Dashboard")
    public String adminDashboardUppercase() {
        System.out.println("Admin Dashboard uppercase - redirecting to lowercase");
        return "redirect:/admin/dashboard";
    }
    
    // Admin pages - simple implementations
    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        System.out.println("Admin Users page - direct");
        model.addAttribute("email", "admin@example.com");
        return "admin-users";
    }
    
    @GetMapping("/Admin/users")
    public String adminUsersUppercase() {
        return "redirect:/admin/users";
    }
    
    @GetMapping("/admin/data")
    public String adminData(Model model) {
        System.out.println("Admin Data page - direct");
        model.addAttribute("email", "admin@example.com");
        return "admin-data";
    }
    
    @GetMapping("/Admin/data")
    public String adminDataUppercase() {
        return "redirect:/admin/data";
    }
    
    @GetMapping("/admin/crops")
    public String adminCrops(Model model) {
        System.out.println("Admin Crops page - direct");
        model.addAttribute("email", "admin@example.com");
        return "admin-crops";
    }
    
    @GetMapping("/Admin/crops")
    public String adminCropsUppercase() {
        return "redirect:/admin/crops";
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
}
