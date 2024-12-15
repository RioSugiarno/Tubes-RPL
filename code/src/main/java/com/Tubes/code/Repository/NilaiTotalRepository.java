package com.Tubes.code.Repository;

import java.math.BigDecimal;

import com.Tubes.code.Entity.NilaiTotal;

public interface NilaiTotalRepository {
    void saveOrUpdate(NilaiTotal nilaiTotal);
    NilaiTotal findByIdTa(int idTa);
    BigDecimal findTotalScoreByIdTa(int idTa);
}
