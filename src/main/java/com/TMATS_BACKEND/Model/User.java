package com.TMATS_BACKEND.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String department;
    private String academicYear;
    private String classSection;
    private String mobileNumber;
    
    @Column(nullable = false)
    private boolean verified = false;

    @Enumerated(EnumType.STRING)
    private Role role;
}
