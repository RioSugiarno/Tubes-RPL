package com.Tubes.code.Repository;

import com.Tubes.code.Entity.KomponenNilai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ImplKomponenNilaiRepository implements KomponenNilaiRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private KomponenNilai mapRowToKomponenNilai(ResultSet rs, int rowNum) throws SQLException {
        return new KomponenNilai(
                rs.getInt("id_nilai"),
                rs.getString("komponen"),
                rs.getDouble("bobot")
        );
    };

    public List<KomponenNilai> findAll() {
        String sql = "SELECT * FROM KomponenNilai";
        return jdbcTemplate.query(sql, this::mapRowToKomponenNilai);
    }

    public KomponenNilai findById(int id) {
        String sql = "SELECT * FROM KomponenNilai WHERE ID_Nilai = ?";
        return jdbcTemplate.queryForObject(sql,this::mapRowToKomponenNilai, id);
    }

    public int save(KomponenNilai komponenNilai) {
        String sql = "INSERT INTO KomponenNilai (Komponen, Bobot) VALUES (?, ?)";
        return jdbcTemplate.update(sql, komponenNilai.getKomponen(), komponenNilai.getBobot());
    }

    public int update(KomponenNilai komponenNilai) {
        String sql = "UPDATE KomponenNilai SET Komponen = ?, Bobot = ? WHERE ID_Nilai = ?";
        return jdbcTemplate.update(sql, komponenNilai.getKomponen(), komponenNilai.getBobot(), komponenNilai.getIdNilai());
    }

    public int delete(int id) {
        String sql = "DELETE FROM KomponenNilai WHERE ID_Nilai = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int setBobotById(int id, Double bobot) {
        try {
            String sql = "UPDATE KomponenNilai SET Bobot = ? WHERE ID_Nilai = ?";
            jdbcTemplate.update(sql, bobot, id);
        }catch (Exception e){
            e.getCause();
        }
        return 0;
    }
}
