package com.TMATS_BACKEND.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.TMATS_BACKEND.Model.Role;

import java.util.Enumeration;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, HttpServletRequest request, HttpSession session) {
        // Print all session attributes for debugging
        System.out.println("==== SESSION ATTRIBUTES ====");
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            Object value = session.getAttribute(name);
            System.out.println(name + " = " + value + " (" + (value != null ? value.getClass().getName() : "null") + ")");
        }
        System.out.println("===========================");
        
        // Get the specific attributes we need
        Object user = session.getAttribute("user");
        Object role = session.getAttribute("role");
        
        System.out.println("NoHandlerFoundException for path: " + request.getRequestURI());
        
        if (user != null) {
            // Direct comparison with Role enum
            if (role != null && role instanceof Role) {
                if (role == Role.ADMIN) {
                    System.out.println("Role is Role.ADMIN enum, redirecting to Admin Dashboard");
                    return "redirect:/Admin/Dashboard";
                }
            }
            
            // Try all other ways to check for admin role
            if (isAdmin(role)) {
                return "redirect:/Admin/Dashboard";
            }
            
            return "redirect:/Dashboard";
        }
        
        return "redirect:/Home";
    }
    
    // Helper method to check for admin role in various formats
    private boolean isAdmin(Object role) {
        if (role == null) return false;
        
        // Check string representation
        String roleStr = role.toString();
        System.out.println("Role as string: " + roleStr);
        
        // Check various possibilities
        if (roleStr.equals("ADMIN")) return true;
        if (roleStr.equalsIgnoreCase("ADMIN")) return true;
        if (roleStr.contains("ADMIN")) return true;
        
        // If the role is an enum, check its name or ordinal
        if (role.getClass().isEnum()) {
            System.out.println("Role is an enum: " + role.getClass().getName());
            try {
                Enum<?> enumRole = (Enum<?>) role;
                System.out.println("Enum name: " + enumRole.name());
                System.out.println("Enum ordinal: " + enumRole.ordinal());
                
                if (enumRole.name().equals("ADMIN")) return true;
            } catch (Exception e) {
                System.out.println("Error checking enum: " + e.getMessage());
            }
        }
        
        return false;
    }
}

/**
 * Custom error controller to handle 404 errors that aren't caught by the controller advice
 */
@Controller
class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpSession session, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        
        if (status != null && Integer.valueOf(status.toString()) == 404) {
            // Print all session attributes for debugging
            System.out.println("==== SESSION ATTRIBUTES (ERROR CONTROLLER) ====");
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                Object value = session.getAttribute(name);
                System.out.println(name + " = " + value + " (" + (value != null ? value.getClass().getName() : "null") + ")");
            }
            System.out.println("===========================");
            
            // Get the specific attributes we need
            Object user = session.getAttribute("user");
            Object role = session.getAttribute("role");
            
            System.out.println("Error controller handling 404 for path: " + request.getRequestURI());
            
            if (user != null) {
                // Direct comparison with Role enum
                if (role != null && role instanceof Role) {
                    if (role == Role.ADMIN) {
                        System.out.println("Role is Role.ADMIN enum, redirecting to Admin Dashboard");
                        return "redirect:/Admin/Dashboard";
                    }
                }
                
                // Try all other ways to check for admin role
                if (isAdmin(role)) {
                    return "redirect:/Admin/Dashboard";
                }
                
                return "redirect:/Dashboard";
            }
            
            return "redirect:/Home";
        }
        
        // For non-404 errors, just redirect to home
        return "redirect:/Home";
    }
    
    // Helper method to check for admin role in various formats
    private boolean isAdmin(Object role) {
        if (role == null) return false;
        
        // Check string representation
        String roleStr = role.toString();
        System.out.println("Role as string: " + roleStr);
        
        // Check various possibilities
        if (roleStr.equals("ADMIN")) return true;
        if (roleStr.equalsIgnoreCase("ADMIN")) return true;
        if (roleStr.contains("ADMIN")) return true;
        
        // If the role is an enum, check its name or ordinal
        if (role.getClass().isEnum()) {
            System.out.println("Role is an enum: " + role.getClass().getName());
            try {
                Enum<?> enumRole = (Enum<?>) role;
                System.out.println("Enum name: " + enumRole.name());
                System.out.println("Enum ordinal: " + enumRole.ordinal());
                
                if (enumRole.name().equals("ADMIN")) return true;
            } catch (Exception e) {
                System.out.println("Error checking enum: " + e.getMessage());
            }
        }
        
        return false;
    }
} 