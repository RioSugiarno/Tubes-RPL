package com.Tubes.code.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String namaLengkap;
    private String username;
    private String password;
    private String role;
}
