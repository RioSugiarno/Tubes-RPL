package com.Tubes.code.Entity;

import lombok.Data;

@Data
public class CatatanSidang {
    private Integer idCatatan;     // ID Catatan Sidang (AUTO_INCREMENT di database)
    private Integer idTa;          // ID Tugas Akhir
    private String catatan1;       // Catatan Sidang oleh Pembimbing 1
    private String catatan2;       // Catatan Sidang oleh Pembimbing 2
    private String nidMahasiswa;   // NIM Mahasiswa
    private String nidPembimbing1; // NID Pembimbing 1
    private String nidPembimbing2; // NID Pembimbing 2
}
