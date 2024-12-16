package com.Tubes.code.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BapService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Pembimbing, Koordinator, Penguji Jalan
    // public void updateBapStatus(String npm, String currentUserId) {
    //     String query = """
    //             UPDATE bap
    //             SET statusPenguji = CASE
    //                                 WHEN NID_Penguji_1 = ? OR NID_Penguji_2 = ? THEN true
    //                                 ELSE statusPenguji END,
    //                 statusPembimbing = CASE
    //                                     WHEN NID_Pembimbing_1 = ? OR NID_Pembimbing_2 = ? THEN true
    //                                     ELSE statusPembimbing END,
    //                 statusMahasiswa = CASE
    //                                   WHEN NID_Mahasiswa = ? THEN true
    //                                   ELSE statusMahasiswa END,
    //                 statusKoordinator = CASE
    //                                      WHEN NID_Koordinator = ? THEN true
    //                                      ELSE statusKoordinator END
    //             WHERE ID_TA = (
    //                 SELECT ID_TA
    //                 FROM informasitugasakhir
    //                 WHERE NIM = ?
    //             );
    //     """;
    
    //     System.out.println("Executing query with parameters:");
    //     System.out.println("currentUserId: " + currentUserId);
    //     System.out.println("npm: " + npm);
    
    //     int rowsUpdated = jdbcTemplate.update(query,
    //             currentUserId, currentUserId, // Untuk statusPenguji
    //             currentUserId, currentUserId, // Untuk statusPembimbing
    //             currentUserId,               // Untuk statusMahasiswa
    //             currentUserId,               // Untuk statusKoordinator
    //             npm                          // Untuk WHERE clause
    //     );
    
    //     System.out.println("Rows updated: " + rowsUpdated);
    // }

    public void updateBapStatus(String npm, String currentUserId) {
        String query = """
            UPDATE bap
            SET 
                statusPenguji = CASE
                    WHEN TRIM(NID_Penguji_1) = TRIM(?) OR TRIM(NID_Penguji_2) = TRIM(?) THEN true
                    ELSE statusPenguji END,
                statusPembimbing = CASE
                    WHEN TRIM(NID_Pembimbing_1) = TRIM(?) OR TRIM(NID_Pembimbing_2) = TRIM(?) THEN true
                    ELSE statusPembimbing END,
                statusMahasiswa = CASE
                    WHEN TRIM(NID_Mahasiswa) = TRIM(?) THEN true
                    ELSE statusMahasiswa END,
                statusKoordinator = CASE
                    WHEN TRIM(NID_Koordinator) = TRIM(?) THEN true
                    ELSE statusKoordinator END
            WHERE ID_TA = (
                SELECT ID_TA
                FROM informasitugasakhir
                WHERE TRIM(NIM) = TRIM(?)
            );
        """;
    
        System.out.println("Executing query with parameters:");
        System.out.println("currentUserId: " + currentUserId);
        System.out.println("npm: " + npm);
    
        int rowsUpdated = jdbcTemplate.update(query,
            currentUserId, currentUserId, // Untuk statusPenguji
            currentUserId, currentUserId, // Untuk statusPembimbing
            currentUserId,                // Untuk statusMahasiswa
            currentUserId,                // Untuk statusKoordinator
            npm                           // Untuk subquery WHERE NIM = ?
        );
    
        System.out.println("Rows updated: " + rowsUpdated);
    }

    public Map<String, Boolean> getBapStatusByNID(String nid, String npm) {
        String query = """
            SELECT statuspenguji, statuspembimbing, statusmahasiswa, statuskoordinator
            FROM bap
            WHERE ID_TA = (
                SELECT ID_TA
                FROM informasitugasakhir
                WHERE NIM = ?
            )
            AND (
                NID_Penguji_1 = ? OR NID_Penguji_2 = ?
                OR NID_Pembimbing_1 = ? OR NID_Pembimbing_2 = ?
                OR NID_Mahasiswa = ? OR NID_Koordinator = ?
            )
            LIMIT 1;
        """;
    
        try {
            return jdbcTemplate.queryForObject(
                query,
                new Object[]{npm, nid, nid, nid, nid, nid, nid},
                (rs, rowNum) -> {
                    Map<String, Boolean> statusMap = new HashMap<>();
                    statusMap.put("statusPenguji", rs.getBoolean("statuspenguji"));
                    statusMap.put("statusPembimbing", rs.getBoolean("statuspembimbing"));
                    statusMap.put("statusMahasiswa", rs.getBoolean("statusmahasiswa"));
                    statusMap.put("statusKoordinator", rs.getBoolean("statuskoordinator"));
                    return statusMap;
                }
            );
        } catch (EmptyResultDataAccessException e) {
            // Jika tidak ditemukan data yang sesuai, kembalikan default false untuk semua status
            Map<String, Boolean> defaultStatus = new HashMap<>();
            defaultStatus.put("statusPenguji", false);
            defaultStatus.put("statusPembimbing", false);
            defaultStatus.put("statusMahasiswa", false);
            defaultStatus.put("statusKoordinator", false);
            return defaultStatus;
        }
    }
}