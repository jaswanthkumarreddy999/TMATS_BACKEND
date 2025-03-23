package com.TMATS_BACKEND.DTO;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String token;
    private String password;
} 