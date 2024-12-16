package com.Tubes.code.Repository;

import com.Tubes.code.Entity.PenilaianDetail;

import java.util.List;

public interface PenilaianDetailRepository {
    List<PenilaianDetail> findByIdTa(int idTa);
    int save(PenilaianDetail penilaianDetail);
    double findTotalScoreByIdTa(int idTa);
    
}