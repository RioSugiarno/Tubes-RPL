package com.Tubes.code.Service;

import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Repository.InfoTugasAkhirRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<List<NpmMahasiswaPair>> findPair(){
        Optional<List<NpmMahasiswaPair>> pairs = infoTugasAkhirRepository.createPair();
        if (pairs.isPresent()&&!pairs.get().isEmpty()){
            return pairs;
        }
        return Optional.empty();
    }

    public boolean addDataMahasiswa(String npm, String judul, String jenis, String pembimbing1, String pembimbing2){
        String nid_pembimbing1 = dosenService.getNID(pembimbing1);
        String nid_pembimbing2 = dosenService.getNID(pembimbing2);
        boolean validNpm = mahasiswaService.isPresent(npm);
        if (!nid_pembimbing1.isEmpty()&&!nid_pembimbing2.isEmpty()&&validNpm){
            infoTugasAkhirRepository.insertDataMahasiswa(npm, judul, jenis, nid_pembimbing1, nid_pembimbing2);
            return true;
        }
        return false;
    }

    public boolean addJadwalMahasiswa(String npm, LocalDate tanggal, LocalTime waktu,String tempat, String penguji1, String penguji2){
        String nid_penguji1 = dosenService.getNID(penguji1);
        String nid_penguji2 = dosenService.getNID(penguji2);
        boolean validNpm = mahasiswaService.isPresent(npm);
        if (!nid_penguji1.isEmpty()&&!nid_penguji2.isEmpty()&&validNpm){
            infoTugasAkhirRepository.insertJadwalMahasiswa(npm,tanggal,waktu,tempat,nid_penguji1,nid_penguji2);
            return true;
        }
        return false;
    }

    // public List<InfoTugasAkhir> getSidangByPenguji(String nidPenguji) {
    //     return infoTugasAkhirRepository.findSidangByPenguji(nidPenguji);
    // }

    public List<Map<String, Object>> getSidangByPengujiWithNames(String nidPenguji) {
        return infoTugasAkhirRepository.findSidangByPengujiWithNames(nidPenguji);
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
    
    
}
