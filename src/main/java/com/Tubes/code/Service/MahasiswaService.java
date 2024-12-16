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

    public String findNIMByUsername(String username) {
        Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findByUsername(username);
        return mahasiswa.map(Mahasiswa::getNIM).orElse(null);
    }

    public Mahasiswa findByNim(String nim) {
        Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findByNIM(nim);
        return mahasiswa.orElse(null);
    } 

    
    public Mahasiswa authenticate(String nim, String password) {
        Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findByNIM(nim);
        if (mahasiswa.isPresent() && mahasiswa.get().getPassword().equals(password)) {
            return mahasiswa.get();
        }
        return null; // Return null jika autentikasi gagal
    
    }
    public String getNIM(String nama) {
        Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findNIMByNama(nama);
        if (mahasiswa.isPresent()) {
            System.out.println("Ditemukan NIM: " + mahasiswa.get().getNIM());
            return mahasiswa.get().getNIM();
        }
        System.out.println("NIM tidak ditemukan untuk nama: " + nama);
        return null;
    }
}
