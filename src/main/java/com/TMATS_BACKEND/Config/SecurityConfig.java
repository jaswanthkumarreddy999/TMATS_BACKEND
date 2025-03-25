package com.TMATS_BACKEND.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (Enable in production)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/Contact","/Products","/Services","/About","/Home", "/Register", "/Login", 
                                "/forgot-password", "/reset-password", "/forgot-password-submit", "/api/auth/forgot-password", "/api/auth/reset-password",
                                "/css/**", "/js/**", "/images/**", "/static/**", "/fonts/**", "/favicon.ico",
                                "/favicon.png","/UserNotFound","/Wrongpassword","/Dashboard",
                                "/verify-otp", "/api/auth/verify-otp", "/api/auth/resend-otp", "/UserNotVerified", "/user-exists", "/Community",
                                "/debug-session", "/test/**").permitAll()
                .requestMatchers("/Admin/Dashboard", "/admin/dashboard", 
                                 "/Admin/users", "/admin/users", 
                                 "/Admin/data", "/admin/data", 
                                 "/Admin/crops", "/admin/crops").permitAll()
                .requestMatchers("/user/profile", "/user/crops", "/user/analytics").permitAll()
                .anyRequest().authenticated() // Protect all other routes
            )
            .formLogin(form -> form
                .loginPage("/Home") // Your custom login page
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/Home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .defaultAuthenticationEntryPointFor(
                    (request, response, exception) -> {
                        // This will handle unauthorized access attempts
                        System.out.println("Security exception handler triggered: " + exception.getMessage());
                        if (request.getSession().getAttribute("user") != null) {
                            response.sendRedirect("/Dashboard");
                        } else {
                            response.sendRedirect("/Home");
                        }
                    },
                    new AntPathRequestMatcher("/**")
                )
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
