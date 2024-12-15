package com.Tubes.code.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import com.Tubes.code.Service.DosenService;

@Data
@AllArgsConstructor
public class InfoTugasAkhir {
    private int idTa;
    private String npm;
    private String judul;
    private String nid_pembimbing1;
    private String nid_pembimbing2;
    private String nid_penguji1;
    private String nid_penguji2;
    private String tempat;
    private LocalDate tanggal;
    private String jenisTA;
    private LocalTime waktu;
}
