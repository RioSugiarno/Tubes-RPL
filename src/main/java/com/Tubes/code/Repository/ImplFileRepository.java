package com.Tubes.code.Repository;

import com.Tubes.code.Entity.InfoTugasAkhir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ImplFileRepository implements FileRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int findIdByJudul(String judul) {
        String query = "SELECT id_ta FROM informasitugasakhir WHERE judul = ?";
        List<InfoTugasAkhir> result = jdbcTemplate.query(query,this::mapRowToInfoTugasAkhir, judul);
        if(!result.isEmpty()) {
            return result.get(0).getIdTa();
        }
        return -1;
    }

    private InfoTugasAkhir mapRowToInfoTugasAkhir(ResultSet rs, int rowNum) throws SQLException {
        return new InfoTugasAkhir(
                rs.getInt("id_ta"),
                rs.getString("nim"),
                rs.getString("judul"),
                rs.getString("nid_pembimbing1"),
                rs.getString("nid_pembimbing2"),
                rs.getString("nid_penguji1"),
                rs.getString("nid_penguji2"),
                rs.getString("tempat"),
                rs.getDate("tanggal").toLocalDate(),
                rs.getString("jenista"),
                rs.getTime("waktu").toLocalTime()
        );
    }
}
