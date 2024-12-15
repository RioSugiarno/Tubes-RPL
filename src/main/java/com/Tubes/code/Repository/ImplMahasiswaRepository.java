package com.Tubes.code.Repository;

import com.Tubes.code.Entity.Mahasiswa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ImplMahasiswaRepository implements MahasiswaRepository {

    @Autowired
    private JdbcTemplate template;

    @Override
    public void save(Mahasiswa mahasiswa) throws Exception {
    }

    @Override
    public Optional<Mahasiswa> findByUsername(String username) {
        String sql = "SELECT * FROM mahasiswa WHERE username = ?";
        List<Mahasiswa> result = template.query(sql,this::mapRowToMahasiswa,username);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<Mahasiswa> findByNIM(String NIM) {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        List<Mahasiswa> result = template.query(sql,this::mapRowToMahasiswa,NIM);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<Mahasiswa> findNIMByNama(String name) {
        String sql = "SELECT * FROM mahasiswa WHERE nama = ?";
        List<Mahasiswa> result = template.query(sql,this::mapRowToMahasiswa,name);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    private Mahasiswa mapRowToMahasiswa(ResultSet rs, int rowNum) throws SQLException {
        return new Mahasiswa(
                rs.getString("nim"),
                rs.getString("nama"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}
