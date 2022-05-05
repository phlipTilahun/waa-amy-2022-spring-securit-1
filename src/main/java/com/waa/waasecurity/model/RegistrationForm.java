package com.waa.waasecurity.model;

import lombok.Data;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String repassword;
}