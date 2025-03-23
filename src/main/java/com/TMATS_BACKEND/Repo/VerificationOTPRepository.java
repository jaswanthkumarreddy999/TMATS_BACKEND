package com.TMATS_BACKEND.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.TMATS_BACKEND.Model.User;
import com.TMATS_BACKEND.Model.VerificationOTP;

@Repository
public interface VerificationOTPRepository extends JpaRepository<VerificationOTP, Long> {
    
    Optional<VerificationOTP> findByOtp(String otp);
    
    Optional<VerificationOTP> findByUser(User user);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationOTP o WHERE o.user = :user")
    void deleteByUser(@Param("user") User user);
} 