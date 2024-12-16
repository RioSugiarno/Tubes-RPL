package com.Tubes.code.Repository;

import com.Tubes.code.Entity.CatatanSidang;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public class CatatanSidangRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // public int save(CatatanSidang catatan) {
    //     String sql = "INSERT INTO CatatanSidang (ID_TA, Catatan, NID_Mahasiswa, NID_Penguji) VALUES (?, ?, ?, ?)";
    //     System.out.println("Executing query: " + sql);
    //     System.out.println("With parameters: ID_TA=" + catatan.getIdTa() + ", Catatan=" + catatan.getCatatan() +
    //             ", NID_Mahasiswa=" + catatan.getNidMahasiswa() + ", NID_Penguji=" + catatan.getNidPenguji());
    //     try {
    //         return jdbcTemplate.update(sql,
    //                 catatan.getIdTa(),
    //                 catatan.getCatatan(),
    //                 catatan.getNidMahasiswa(),
    //                 catatan.getNidPenguji());
    //     } catch (Exception e) {
    //         System.err.println("Error executing query: " + e.getMessage());
    //         throw e;
    //     }
    // } 

    // Database Catatan TA Baru - Catatan 1 Bisa
    public void insertOrUpdateCatatanTa(int idTa, String nidPembimbing1, String nidPembimbing2, String npm, String catatan, String loggedInNid) {
        // Query untuk update catatan jika sudah ada
        String query = """
                UPDATE catatansidang
                SET Catatan1 = CASE WHEN NID_Pembimbing1 = ? THEN ? ELSE Catatan1 END,
                    Catatan2 = CASE WHEN NID_Pembimbing2 = ? THEN ? ELSE Catatan2 END
                WHERE ID_TA = ?;
        """;

        int rowsUpdated = jdbcTemplate.update(query, loggedInNid, catatan, loggedInNid, catatan, idTa);

        // Jika tidak ada baris yang diperbarui, lakukan INSERT
        if (rowsUpdated == 0) {
            query = """
                    INSERT INTO catatansidang (ID_TA, NID_Pembimbing1, NID_Pembimbing2, NID_Mahasiswa, Catatan1, Catatan2)
                    VALUES (?, ?, ?, ?, '', '');
            """;
            jdbcTemplate.update(query, idTa, nidPembimbing1, nidPembimbing2, npm);
            System.out.println("Rows inserted into catatansidang.");
        } else {
            System.out.println("Rows updated in catatansidang.");
        }
    }

    public Map<String, String> findCatatanByMahasiswaAndPembimbing(String npm, String nidPembimbing) {
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
            return jdbcTemplate.queryForObject(query, new Object[] { npm, nidPembimbing, nidPembimbing },
                    (rs, rowNum) -> {
                        Map<String, String> catatanMap = new HashMap<>();
                        catatanMap.put("Catatan1", rs.getString("Catatan1"));
                        catatanMap.put("Catatan2", rs.getString("Catatan2"));
                        return catatanMap;
                    });
        } catch (EmptyResultDataAccessException e) {
            System.out.println(
                    "Tidak ditemukan catatan untuk mahasiswa: " + npm + " dengan pembimbing: " + nidPembimbing);
            Map<String, String> defaultCatatan = new HashMap<>();
            defaultCatatan.put("Catatan1", "");
            defaultCatatan.put("Catatan2", "");
            return defaultCatatan;
        }
    }
}