package com.Tubes.code.Repository;

import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.NpmMahasiswaPair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InfoTugasAkhirRepository {
    boolean insertDataMahasiswa(String npm, String judul, String jenis, String pembimbing1, String pembimbing2);
    boolean insertJadwalMahasiswa(String npm, LocalDate tanggal, LocalTime waktu, String tempat, String penguji1, String penguji2);
    Optional<InfoTugasAkhir> getInfoTugasAkhir(String judul);
    Optional<List<NpmMahasiswaPair>> createPair();

    // Informasi Sidang Penguji NID
    // List<InfoTugasAkhir> findSidangByPenguji(String nidPenguji);
    List<Map<String, Object>> findSidangByPengujiWithNames(String nidPenguji);

    // Input nilai sidang penguji
    int findIdTaByNpm(String npm); // Hapus tubuh method
    int findIdKomponenNilaiByDeskripsi(String deskripsi); // Hapus tubuh method
    InfoTugasAkhir findByNpm(String npm);
}
