package com.Tubes.code.Service;

import com.Tubes.code.Entity.PenilaianDetail;
import com.Tubes.code.Repository.PenilaianDetailRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PenilaianDetailService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isAlreadyEvaluated(int idTa, String nidPenilai) {
        String query = "SELECT COUNT(*) FROM PenilaianDetail WHERE ID_TA = ? AND NID_Penilai = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, idTa, nidPenilai);
        return count != null && count > 0;
    }

    @Transactional
    public void saveAllPenilaianDetails(int idTa, String nidPenilai, Map<Integer, Double> nilaiMap) {
        String query = "INSERT INTO PenilaianDetail (ID_TA, ID_Nilai, NID_Penilai, Nilai) VALUES (?, ?, ?, ?)";
        nilaiMap.forEach((idNilai, nilai) -> {
            jdbcTemplate.update(query, idTa, idNilai, nidPenilai, nilai);
        });
    }
}
