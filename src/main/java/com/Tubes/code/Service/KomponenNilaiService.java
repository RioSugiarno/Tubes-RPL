package com.Tubes.code.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class KomponenNilaiService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void inisialisasiKomponenNilai(int idTa) {
        String query = "INSERT INTO KomponenNilai (ID_TA, Komponen, Nilai) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, idTa, "Presentasi", 0.0);
        jdbcTemplate.update(query, idTa, "Tata Tulis Laporan", 0.0);
        jdbcTemplate.update(query, idTa, "Kelengkapan Materi", 0.0);
        jdbcTemplate.update(query, idTa, "Pencapaian Tujuan", 0.0);
        jdbcTemplate.update(query, idTa, "Penguasaan Materi", 0.0);
    }
}
