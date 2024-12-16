package com.Tubes.code.Service;

import com.Tubes.code.Entity.KomponenNilai;
import com.Tubes.code.Repository.KomponenNilaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KomponenNilaiService {

    @Autowired
    KomponenNilaiRepository repository;

    public KomponenNilaiService(KomponenNilaiRepository repository) {
        this.repository = repository;
    }

    public void setBobotById(int id, Double bobot) {
        repository.setBobotById(id, bobot);
    }

    public List<KomponenNilai> getAllKomponenNilai() {
        return repository.findAll();
    }

    public KomponenNilai getKomponenNilaiById(int id) {
        return repository.findById(id);
    }

    public void addKomponenNilai(KomponenNilai komponenNilai) {
        repository.save(komponenNilai);
    }

    public void updateKomponenNilai(KomponenNilai komponenNilai) {
        repository.update(komponenNilai);
    }

    public void deleteKomponenNilai(int id) {
        repository.delete(id);
    }

    public List<Map<String, Object>> getKomponenNilaiAsMap() {
        List<KomponenNilai> komponenList = repository.findAll();

        return komponenList.stream().map(komponen -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idNilai", komponen.getIdNilai());
            map.put("bobot", komponen.getBobot());
            return map;
        }).toList();
    }
}
