package com.Tubes.code.Service;

import com.Tubes.code.Entity.NilaiTotal;
import com.Tubes.code.Repository.NilaiTotalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NilaiTotalService {

    @Autowired
    private NilaiTotalRepository nilaiTotalRepository;

    // Method untuk mengambil nilai total berdasarkan idTa
    public BigDecimal getTotalNilaiByIdTa(Integer idTa) {
        NilaiTotal nilaiTotal = nilaiTotalRepository.findByIdTa(idTa);
        if (nilaiTotal != null) {
            return nilaiTotal.getTotal();  // Mengambil total nilai dari entitas NilaiTotal
        } else {
            return BigDecimal.ZERO;  // Jika tidak ditemukan, mengembalikan nilai default 0
        }
    }
}
