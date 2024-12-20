package com.Tubes.code.Repository;

import com.Tubes.code.Entity.KomponenNilai;

import java.math.BigDecimal;
import java.util.List;

public interface KomponenNilaiRepository {
    List<KomponenNilai> findAll();
    KomponenNilai findById(int id);
    int save(KomponenNilai komponenNilai);
    int update(KomponenNilai komponenNilai);
    int delete(int id);
    int setBobotById(int id, Double bobot);
    String findKomponenById(int idNilai);
    BigDecimal findBobotByIdNilai(int idNilai);
}
