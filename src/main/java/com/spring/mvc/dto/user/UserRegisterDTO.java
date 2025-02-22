package com.spring.mvc.dto.user;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String name;
    private String email;
    private String password;
    private String address;
    private String sdt;
}

