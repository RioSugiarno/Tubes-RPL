package com.Tubes.code.Service;

import com.Tubes.code.Repository.MahasiswaRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.Tubes.code.Entity.Mahasiswa;

@Service
public class MahasiswaService {

    @Autowired
    MahasiswaRepository mahasiswaRepository;

    public boolean login(){
        return true;
    }

    public boolean isPresent (String nim){
        return mahasiswaRepository.findByNIM(nim).isPresent();
    }

    public String getNIM(String nama) {
        Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findNIMByNama(nama);
        if (mahasiswa.isPresent()) {
            System.out.println("Ditemukan NIM: " + mahasiswa.get().getNIM()); // Debug log
            return mahasiswa.get().getNIM();
        }
        System.out.println("NIM tidak ditemukan untuk nama: " + nama); // Debug log
        return null;
    }
}
