package com.Tubes.code.Service;

import com.Tubes.code.Entity.Dosen;
import com.Tubes.code.Repository.DosenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DosenService {

    @Autowired
    DosenRepository dosenRepository;

    // public String getNID(String nama){
    //     Optional<Dosen> dosen = dosenRepository.findNIDByNama(nama);
    //     return dosen.map(Dosen::getNID).orElse(null);
    // }

    public String getNID(String nama) {
        Optional<Dosen> dosen = dosenRepository.findNIDByNama(nama);
        if (dosen.isPresent()) {
            System.out.println("Ditemukan NID: " + dosen.get().getNID());
        }
        System.out.println("NID tidak ditemukan untuk nama: " + nama);
        return null;
    }

    public List<Dosen> getPembimbingByMahasiswa(String nimMahasiswa) {
        List<Dosen> pembimbingList = dosenRepository.findPembimbingByMahasiswa(nimMahasiswa);
        System.out.println("Pembimbing dari Service: " + pembimbingList);
        return pembimbingList;
    } 
}
