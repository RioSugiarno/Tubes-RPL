package com.Tubes.code.Entity;

import lombok.Data;

@Data
public class CatatanSidang {
    private Integer idCatatan;     // ID Catatan Sidang (AUTO_INCREMENT di database)
    private Integer idTa;          // ID Tugas Akhir
    private String catatan;        // Catatan Sidang
    private String nidMahasiswa;   // NIM Mahasiswa
    private String nidPenguji;     // NID Penguji
}
