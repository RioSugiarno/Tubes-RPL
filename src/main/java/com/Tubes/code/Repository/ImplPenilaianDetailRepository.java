package com.Tubes.code.Repository;

import com.Tubes.code.Entity.PenilaianDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ImplPenilaianDetailRepository implements PenilaianDetailRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PenilaianDetail mapRowToPenilaianDetail(ResultSet rs, int rowNum) throws SQLException {
    return new PenilaianDetail(
            rs.getInt("id_penilaian"),
            rs.getInt("id_ta"),
            rs.getInt("id_nilai"),
            rs.getString("nid_penilai"),
            BigDecimal.valueOf(rs.getDouble("nilai")) // Convert to BigDecimal
        );
    }

    // @Override
    // public List<PenilaianDetail> findByIdTa(int idTa) {
    //     String sql = "SELECT * FROM PenilaianDetail WHERE ID_TA = ?";
    //     return jdbcTemplate.query(sql, this::mapRowToPenilaianDetail, idTa);
    // }

    @Override
    public List<PenilaianDetail> findByIdTa(int idTa) {
        String sql = "SELECT * FROM PenilaianDetail WHERE id_ta = ?";
        return jdbcTemplate.query(sql, new Object[]{idTa}, (rs, rowNum) ->
                new PenilaianDetail(
                        rs.getInt("id_penilaian"),
                        rs.getInt("id_ta"),
                        rs.getInt("id_nilai"),
                        rs.getString("nid_penilai"),
                        rs.getBigDecimal("nilai")
                )
        );
    }

    @Override
    public int save(PenilaianDetail penilaianDetail) {
        String sql = "INSERT INTO PenilaianDetail (ID_TA, ID_Nilai, NID_Penilai, Nilai) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, penilaianDetail.getIdTa(), penilaianDetail.getIdNilai(), penilaianDetail.getNidPenilai(), penilaianDetail.getNilai());
    }

    @Override
    public double findTotalScoreByIdTa(int idTa) {
        String sql = "SELECT SUM(pd.Nilai * kn.Bobot) AS TotalScore " +
                     "FROM PenilaianDetail pd " +
                     "JOIN KomponenNilai kn ON pd.ID_Nilai = kn.ID_Nilai " +
                     "WHERE pd.ID_TA = ?";
        Double totalScore = jdbcTemplate.queryForObject(sql, Double.class, idTa);
        return totalScore != null ? totalScore : 0.0;
    }
}
