package com.Tubes.code.Repository;

import com.Tubes.code.Entity.NilaiTotal;

import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ImplNilaiTotalRepository implements NilaiTotalRepository {

    private static final Logger log = LoggerFactory.getLogger(ImplNilaiTotalRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public ImplNilaiTotalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // @Override
    // public void saveOrUpdate(NilaiTotal nilaiTotal) {
    //     String sql = """
    //         INSERT INTO NilaiTotal (ID_TA, Total, LastUpdated)
    //         VALUES (?, ?, CURRENT_TIMESTAMP)
    //         ON CONFLICT (ID_TA)
    //         DO UPDATE SET Total = EXCLUDED.Total, LastUpdated = EXCLUDED.LastUpdated
    //     """;
    //     jdbcTemplate.update(sql, nilaiTotal.getIdTa(), nilaiTotal.getTotal());
    // }

    @Override
    public void saveOrUpdate(NilaiTotal nilaiTotal) {
        String fetchSql = "SELECT Total FROM NilaiTotal WHERE ID_TA = ?";
        BigDecimal previousTotal = null;
        try {
            previousTotal = jdbcTemplate.queryForObject(fetchSql, BigDecimal.class, nilaiTotal.getIdTa());
        } catch (EmptyResultDataAccessException e) {
            log.info("NilaiTotal belum ada untuk ID_TA: {}. Menambahkan nilai baru.", nilaiTotal.getIdTa());
        }

        BigDecimal newTotal = (previousTotal != null ? previousTotal : BigDecimal.ZERO).add(nilaiTotal.getTotal());

        String updateSql = """
            INSERT INTO NilaiTotal (ID_TA, Total, LastUpdated)
            VALUES (?, ?, CURRENT_TIMESTAMP)
            ON CONFLICT (ID_TA)
            DO UPDATE SET Total = EXCLUDED.Total, LastUpdated = EXCLUDED.LastUpdated
        """;

        log.info("Menyimpan NilaiTotal baru: ID_TA={}, Total={}", nilaiTotal.getIdTa(), newTotal);
        jdbcTemplate.update(updateSql, nilaiTotal.getIdTa(), newTotal);
    }

    // @Override
    // public NilaiTotal findByIdTa(int idTa) {
    //     String sql = "SELECT * FROM NilaiTotal WHERE ID_TA = ?";
    //     return jdbcTemplate.queryForObject(sql, this::mapRowToNilaiTotal, idTa);
    // }

    @Override
    public NilaiTotal findByIdTa(int idTa) {
        String sql = "SELECT * FROM NilaiTotal WHERE ID_TA = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToNilaiTotal, idTa);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Tidak ada data ditemukan untuk ID_TA: {}", idTa);
            return null;
        }
    }

    private NilaiTotal mapRowToNilaiTotal(ResultSet rs, int rowNum) throws SQLException {
        NilaiTotal nilaiTotal = new NilaiTotal();
        nilaiTotal.setIdTotal(rs.getInt("IdTotal"));
        nilaiTotal.setIdTa(rs.getInt("ID_TA"));
        nilaiTotal.setTotal(rs.getBigDecimal("Total"));
        nilaiTotal.setLastUpdated(rs.getTimestamp("LastUpdated").toLocalDateTime());
        return nilaiTotal;
    }

    @Override
    public BigDecimal findTotalScoreByIdTa(int idTa) {
        String sql = "SELECT total FROM NilaiTotal WHERE id_ta = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idTa}, BigDecimal.class);
    }

}
