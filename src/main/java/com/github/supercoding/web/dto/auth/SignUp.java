package com.github.supercoding.web.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class SignUp {

    private String email;
    private String password;
    private String name;

}
