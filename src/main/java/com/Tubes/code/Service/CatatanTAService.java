package com.Tubes.code.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CatatanTAService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void initializeCatatanTa(int idTa, String nidPembimbing1, String nidPembimbing2) {
        String query = """
                INSERT INTO catatansidang (ID_TA, NID_Pembimbing1, NID_Pembimbing2, Catatan1, Catatan2)
                VALUES (?, ?, ?, '', '');
        """;

        int rowsInserted = jdbcTemplate.update(query, idTa, nidPembimbing1, nidPembimbing2);
        System.out.println("Rows inserted into catatansidang: " + rowsInserted);
    }

    public void updateCatatan(String npm, String currentUserId, String newCatatan) {
        String query = """
                UPDATE catatansidang
                SET Catatan1 = CASE WHEN NID_Pembimbing1 = ? THEN ? ELSE Catatan1 END,
                    Catatan2 = CASE WHEN NID_Pembimbing2 = ? THEN ? ELSE Catatan2 END
                WHERE ID_TA = (
                    SELECT ID_TA
                    FROM informasitugasakhir
                    WHERE NIM = ?
                );
        """;

        System.out.println("Executing query with parameters:");
        System.out.println("currentUserId: " + currentUserId);
        System.out.println("newCatatan: " + newCatatan);
        System.out.println("npm: " + npm);

        int rowsUpdated = jdbcTemplate.update(query,
                currentUserId, newCatatan, // Untuk Catatan1
                currentUserId, newCatatan, // Untuk Catatan2
                npm                       // Untuk WHERE clause
        );

        System.out.println("Rows updated: " + rowsUpdated);
    }

    public Map<String, String> getCatatanByNID(String nid, String npm) {
        String query = """
            SELECT Catatan1, Catatan2
            FROM catatansidang
            WHERE ID_TA = (
                SELECT ID_TA
                FROM informasitugasakhir
                WHERE NIM = ?
            )
            AND (NID_Pembimbing1 = ? OR NID_Pembimbing2 = ?)
            LIMIT 1;
        """;

        try {
            return jdbcTemplate.queryForObject(
                query,
                new Object[]{npm, nid, nid},
                (rs, rowNum) -> {
                    Map<String, String> catatanMap = new HashMap<>();
                    catatanMap.put("Catatan1", rs.getString("Catatan1"));
                    catatanMap.put("Catatan2", rs.getString("Catatan2"));
                    return catatanMap;
                }
            );
        } catch (EmptyResultDataAccessException e) {
            // Jika tidak ditemukan data yang sesuai, kembalikan default nilai kosong
            Map<String, String> defaultCatatan = new HashMap<>();
            defaultCatatan.put("Catatan1", "");
            defaultCatatan.put("Catatan2", "");
            return defaultCatatan;
        }
    }
}