package com.Tubes.code.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
// Entity Class (KomponenNilai)
@Data
@AllArgsConstructor
public class KomponenNilai {
    private int idNilai;
    private String komponen;
    private Double bobot;
}
