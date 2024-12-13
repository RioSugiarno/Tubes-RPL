package com.Tubes.code.Repository;

import com.Tubes.code.Entity.CatatanSidang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CatatanSidangRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // public int save(CatatanSidang catatan) {
    //     String sql = "INSERT INTO CatatanSidang (ID_TA, Catatan, NID_Mahasiswa, NID_Penguji) VALUES (?, ?, ?, ?)";
    //     return jdbcTemplate.update(sql,
    //             catatan.getIdTa(),
    //             catatan.getCatatan(),
    //             catatan.getNidMahasiswa(),
    //             catatan.getNidPenguji());
    // }

    public int save(CatatanSidang catatan) {
        String sql = "INSERT INTO CatatanSidang (ID_TA, Catatan, NID_Mahasiswa, NID_Penguji) VALUES (?, ?, ?, ?)";
        System.out.println("Executing query: " + sql);
        System.out.println("With parameters: ID_TA=" + catatan.getIdTa() + ", Catatan=" + catatan.getCatatan() +
                ", NID_Mahasiswa=" + catatan.getNidMahasiswa() + ", NID_Penguji=" + catatan.getNidPenguji());
        try {
            return jdbcTemplate.update(sql,
                    catatan.getIdTa(),
                    catatan.getCatatan(),
                    catatan.getNidMahasiswa(),
                    catatan.getNidPenguji());
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            throw e;
        }
    } 
}