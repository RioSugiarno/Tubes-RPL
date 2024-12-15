package com.Tubes.code.Repository;

import com.Tubes.code.Entity.Dosen;
import com.Tubes.code.Entity.Mahasiswa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ImplDosenRepository implements DosenRepository{

    @Autowired
    private JdbcTemplate template;

    @Override
    public Optional<Dosen> findByUsername(String username) {
        String sql = "SELECT * FROM dosen WHERE username = ?";
        List<Dosen> result = template.query(sql,this::mapRowToDosen,username);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<Dosen> findNIDByNama(String name) {
        String sql = "SELECT * FROM dosen WHERE nama = ?";
        List<Dosen> result = template.query(sql,this::mapRowToDosen,name);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Dosen> findPembimbingByMahasiswa(String nimMahasiswa) {
        String query = """
            SELECT d.NID, d.Nama, d.username, d.password, d.role
            FROM Dosen d
            JOIN InformasiTugasAkhir ta ON (d.NID = ta.nid_pembimbing1 OR d.NID = ta.nid_pembimbing2)
            WHERE ta.nim = ?
        """;

        List<Dosen> pembimbingList = template.query(query, (rs, rowNum) -> new Dosen(
                rs.getString("NID"),
                rs.getString("Nama"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        ), nimMahasiswa);

        System.out.println("Pembimbing dari Repository: " + pembimbingList); // Debugging
        return pembimbingList;
    }

    private Dosen mapRowToDosen(ResultSet rs, int rowNum) throws SQLException {
        return new Dosen(
                rs.getString("nid"),
                rs.getString("nama"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        );
    }

}
