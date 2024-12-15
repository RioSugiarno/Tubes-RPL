package com.Tubes.code.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dosen {
    private String NID;
    private String Nama;
    private String username;
    private String password;
    private String role;
}
