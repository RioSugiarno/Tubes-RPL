package com.Tubes.code.Service;

import com.Tubes.code.Repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
