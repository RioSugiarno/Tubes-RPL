package com.Tubes.code.Service;

import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.NilaiTotal;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Repository.InfoTugasAkhirRepository;
import com.Tubes.code.Repository.NilaiTotalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InfoTugasAkhirService {
    @Autowired
    InfoTugasAkhirRepository infoTugasAkhirRepository;
    @Autowired
    private MahasiswaService mahasiswaService;
    @Autowired
    private DosenService dosenService;
    @Autowired
    private NilaiTotalRepository nilaiTotalRepository;

    public Optional<List<NpmMahasiswaPair>> findPair(){
        Optional<List<NpmMahasiswaPair>> pairs = infoTugasAkhirRepository.createPair();
        if (pairs.isPresent()&&!pairs.get().isEmpty()){
            return pairs;
        }
        return Optional.empty();
    }

    // Add Data Mahasiswa dengan Inisialisasi BAP & Catatan TA
    // public boolean addDataMahasiswa(String npm, String judul, String jenis, String pembimbing1, String pembimbing2) {
    //     try {
    //         String nidPembimbing1 = dosenService.getNID(pembimbing1);
    //         String nidPembimbing2 = dosenService.getNID(pembimbing2);
    //         boolean validNpm = mahasiswaService.isPresent(npm);
    
    //         if (!nidPembimbing1.isEmpty() && !nidPembimbing2.isEmpty() && validNpm) {
    //             // Tambahkan data mahasiswa ke tabel InformasiTugasAkhir
    //             infoTugasAkhirRepository.insertDataMahasiswa(npm, judul, jenis, nidPembimbing1, nidPembimbing2);
    
    //             // Ambil ID_TA untuk data mahasiswa yang baru saja ditambahkan
    //             int idTa = infoTugasAkhirRepository.findIdTaByNpm(npm);
    
    //             // Inisialisasi tabel BAP
    //             infoTugasAkhirRepository.insertBap(
    //                 idTa,
    //                 null, // NID_Penguji_1 akan diisi nanti
    //                 null, // NID_Penguji_2 akan diisi nanti
    //                 nidPembimbing1,
    //                 nidPembimbing2,
    //                 npm,
    //                 "7182201001" // ID Koordinator
    //             );
    
    //             // Inisialisasi atau perbarui tabel Catatan TA
    //             infoTugasAkhirRepository.insertOrUpdateCatatanTa(
    //                 idTa,
    //                 nidPembimbing1,
    //                 nidPembimbing2,
    //                 npm,
    //                 "", // Catatan awal kosong
    //                 ""
    //             );
    
    //             return true;
    //         }
    //         return false;
    //     } catch (Exception e) {
    //         throw new RuntimeException("Gagal menambahkan data mahasiswa dan menginisialisasi Catatan TA: " + e.getMessage(), e);
    //     }
    // }

    // Add Data Mahasiswa Test dengan NilaiTotal
    public boolean addDataMahasiswa(String npm, String judul, String jenis, String pembimbing1, String pembimbing2) {
        try {
            String nidPembimbing1 = dosenService.getNID(pembimbing1);
            String nidPembimbing2 = dosenService.getNID(pembimbing2);
            boolean validNpm = mahasiswaService.isPresent(npm);

            if (!nidPembimbing1.isEmpty() && !nidPembimbing2.isEmpty() && validNpm) {
                // Tambahkan data mahasiswa ke tabel InformasiTugasAkhir
                infoTugasAkhirRepository.insertDataMahasiswa(npm, judul, jenis, nidPembimbing1, nidPembimbing2);

                // Ambil ID_TA untuk data mahasiswa yang baru saja ditambahkan
                int idTa = infoTugasAkhirRepository.findIdTaByNpm(npm);

                // Inisialisasi tabel NilaiTotal
                NilaiTotal nilaiTotal = new NilaiTotal();
                nilaiTotal.setIdTa(idTa);
                nilaiTotal.setTotal(BigDecimal.ZERO); // Nilai awal 0
                nilaiTotalRepository.saveOrUpdate(nilaiTotal);

                // Inisialisasi tabel BAP
                infoTugasAkhirRepository.insertBap(
                    idTa,
                    null, // NID_Penguji_1 akan diisi nanti
                    null, // NID_Penguji_2 akan diisi nanti
                    nidPembimbing1,
                    nidPembimbing2,
                    npm,
                    "7182201001" // ID Koordinator
                );

                // Inisialisasi atau perbarui tabel Catatan TA
                infoTugasAkhirRepository.insertOrUpdateCatatanTa(
                    idTa,
                    nidPembimbing1,
                    nidPembimbing2,
                    npm,
                    "", // Catatan awal kosong
                    ""
                );

                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menambahkan data mahasiswa dan menginisialisasi tabel: " + e.getMessage(), e);
        }
    }

    // public boolean addJadwalMahasiswa(String npm, LocalDate tanggal, LocalTime waktu,String tempat, String penguji1, String penguji2){
    //     String nid_penguji1 = dosenService.getNID(penguji1);
    //     String nid_penguji2 = dosenService.getNID(penguji2);
    //     boolean validNpm = mahasiswaService.isPresent(npm);
    //     if (!nid_penguji1.isEmpty()&&!nid_penguji2.isEmpty()&&validNpm){
    //         infoTugasAkhirRepository.insertJadwalMahasiswa(npm,tanggal,waktu,tempat,nid_penguji1,nid_penguji2);
    //         return true;
    //     }
    //     return false;
    // }

    // Add Jadwal Mahasiswa dengan Inisialisasi BAP
    public boolean addJadwalMahasiswa(String npm, LocalDate tanggal, LocalTime waktu, String tempat, String penguji1, String penguji2) {
        try {
            // Validasi dosen penguji
            String nidPenguji1 = dosenService.getNID(penguji1);
            String nidPenguji2 = dosenService.getNID(penguji2);
            boolean validNpm = mahasiswaService.isPresent(npm);

            if (!nidPenguji1.isEmpty() && !nidPenguji2.isEmpty() && validNpm) {
                // Perbarui jadwal sidang di tabel InformasiTugasAkhir
                infoTugasAkhirRepository.insertJadwalMahasiswa(npm, tanggal, waktu, tempat, nidPenguji1, nidPenguji2);

                // Ambil ID_TA untuk memperbarui data BAP
                int idTa = infoTugasAkhirRepository.findIdTaByNpm(npm);

                // Perbarui kolom penguji di tabel BAP
                infoTugasAkhirRepository.updatePengujiInBap(idTa, nidPenguji1, nidPenguji2);

                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menambahkan jadwal mahasiswa dan memperbarui BAP: " + e.getMessage(), e);
        }
    }


    //penguji
    public List<Map<String, Object>> getSidangByPengujiWithNames(String nidPenguji) {
        return infoTugasAkhirRepository.findSidangByPengujiWithNames(nidPenguji);
    }

    //pembimbing
    public List<Map<String, Object>> getSidangByPembimbingWithNames(String nidPembimbing) {
        return infoTugasAkhirRepository.findSidangByPembimbingWithNames(nidPembimbing);
    }

    public List<Map<String, Object>> getSidangByMahasiswaWithNames(String nimMahasiswa) {
        return infoTugasAkhirRepository.findSidangByMahasiswaWithNames(nimMahasiswa);
    }

    public int findIdTaByNpm(String npm) {
        return infoTugasAkhirRepository.findIdTaByNpm(npm);
    }

    public int findIdKomponenNilaiByDeskripsi(String deskripsi) {
        return infoTugasAkhirRepository.findIdKomponenNilaiByDeskripsi(deskripsi);
    }

    public int getIdTaByNpm(String npm) {
        // Cari InfoTugasAkhir berdasarkan npm
        InfoTugasAkhir infoTugasAkhir = infoTugasAkhirRepository.findByNpm(npm);
        if (infoTugasAkhir != null) {
            return infoTugasAkhir.getIdTa(); // Mengembalikan idTa dari InfoTugasAkhir
        } else {
            throw new IllegalArgumentException("Data sidang tidak ditemukan untuk NPM: " + npm);
        }
    }
    
    // public InfoTugasAkhir findByNpm(String npm) {
    //     return infoTugasAkhirRepository.findByNpm(npm);
    // }    
    
    public InfoTugasAkhir findByNpm(String npm) {
        InfoTugasAkhir infoTugasAkhir = infoTugasAkhirRepository.findByNpm(npm);
        if (infoTugasAkhir == null) {
            throw new IllegalArgumentException("InfoTugasAkhir tidak ditemukan untuk NPM: " + npm);
        }
        return infoTugasAkhir;
    }

    public int findIdTaByNim(String npm) {
        return infoTugasAkhirRepository.findIdTaByNpm(npm);
    }
}
