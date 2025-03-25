package com.TMATS_BACKEND.Config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.TMATS_BACKEND.Model.Role;

import java.io.IOException;
import java.util.Enumeration;

@Component
@Order(1) // High priority
public class CustomUrlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String path = httpRequest.getRequestURI();
        
        System.out.println("CustomUrlFilter processing path: " + path);
        
        // Skip filtering for admin paths completely - let the controllers handle them
        if (path.startsWith("/admin/") || path.startsWith("/Admin/")) {
            System.out.println("Skipping filter for admin path: " + path);
            chain.doFilter(request, response);
            return;
        }
        
        // Skip processing for static resources
        if (path.startsWith("/css/") || 
            path.startsWith("/js/") || 
            path.startsWith("/images/") || 
            path.startsWith("/static/") ||
            path.contains("favicon") ||
            path.endsWith(".css") ||
            path.endsWith(".js") ||
            path.endsWith(".png") ||
            path.endsWith(".jpg") ||
            path.endsWith(".jpeg") ||
            path.endsWith(".gif") ||
            path.endsWith(".svg") ||
            path.endsWith(".ico")) {
            System.out.println("Skipping filter for static resource: " + path);
            chain.doFilter(request, response);
            return;
        }
        
        // Continue processing if the path matches any of these known endpoints
        if (path.equals("/") || 
            path.equals("/Home") || 
            path.equals("/About") || 
            path.equals("/Services") || 
            path.equals("/Contact") || 
            path.equals("/Login") || 
            path.equals("/Register") || 
            path.equals("/logout") || 
            path.equals("/Dashboard") || 
            path.equals("/debug-session") ||
            path.startsWith("/test/") ||
            path.startsWith("/verify-otp") || 
            path.startsWith("/api/") || 
            path.equals("/error") || 
            path.startsWith("/forgot-password") || 
            path.startsWith("/reset-password") || 
            path.equals("/UserNotFound") || 
            path.equals("/Wrongpassword") || 
            path.equals("/UserNotVerified") || 
            path.equals("/user-exists") ||
            path.equals("/Community")) {
            System.out.println("Continuing normal processing for known path: " + path);
            chain.doFilter(request, response);
            return;
        }
        
        // For all other paths (unknown URLs), check user session and redirect
        if (session != null) {
            System.out.println("==== SESSION ATTRIBUTES IN FILTER ====");
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                Object value = session.getAttribute(name);
                System.out.println(name + " = " + value + " (" + (value != null ? value.getClass().getName() : "null") + ")");
            }
            System.out.println("===========================");
            
            if (session.getAttribute("user") != null) {
                Object role = session.getAttribute("role");
                System.out.println("Unknown URL accessed: " + path);
                System.out.println("User: " + session.getAttribute("user"));
                System.out.println("Role: " + role);
                
                // Not admin, redirect to user dashboard
                System.out.println("REDIRECTING TO USER DASHBOARD from filter");
                httpResponse.sendRedirect("/Dashboard");
                return;
            }
        }
        
        // User not logged in, continue to normal processing
        System.out.println("User not logged in, continuing normal processing");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CustomUrlFilter initialized");
    }

    @Override
    public void destroy() {
        System.out.println("CustomUrlFilter destroyed");
    }
} 