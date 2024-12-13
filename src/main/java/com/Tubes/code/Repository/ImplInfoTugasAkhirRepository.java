package com.Tubes.code.Repository;

import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImplInfoTugasAkhirRepository implements InfoTugasAkhirRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean insertDataMahasiswa(String npm, String judul, String jenis, String pembimbing1, String pembimbing2) {
        try {
            String query = "INSERT INTO informasitugasakhir (nim, judul, jenista, nid_pembimbing1, nid_pembimbing2) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(query,npm, judul, jenis, pembimbing1, pembimbing2);
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertJadwalMahasiswa(String npm, LocalDate tanggal, LocalTime waktu, String tempat, String penguji1, String penguji2) {
        try {
            String query = "UPDATE informasitugasakhir SET tanggal = ?, waktu = ?, tempat = ?, nid_penguji1 = ?, nid_penguji2 = ? " +
                    "WHERE nim = ? AND (tanggal IS NULL AND waktu IS NULL AND tempat IS NULL AND nid_penguji1 IS NULL AND nid_penguji2 IS NULL)";
            jdbcTemplate.update(query,tanggal,waktu,tempat,penguji1,penguji2,npm);
            return true;
        }catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<InfoTugasAkhir> getInfoTugasAkhir(String judul) {
        String query = "SELECT * FROM informasitugasakhir WHERE judul = ?";
        List<InfoTugasAkhir> result = jdbcTemplate.query(query,this::mapRowToInfoTugasAkhir, judul);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<List<NpmMahasiswaPair>> createPair() {
        String query = "SELECT m.NIM, m.Nama FROM mahasiswa m JOIN informasitugasakhir i ON m.NIM = i.NIM";
        List<NpmMahasiswaPair> result = jdbcTemplate.query(query, this::mapRowToNpmMahasiswaPair);
        return result.isEmpty() ? Optional.empty(): Optional.of(result);
    }

    //Penguji Informasi Sidang pake NID
    // @Override
    // public List<InfoTugasAkhir> findSidangByPenguji(String nidPenguji) {
    //     String query = "SELECT * FROM InformasiTugasAkhir " +
    //                 "WHERE nid_penguji1 = ? OR nid_penguji2 = ?";
    //     return jdbcTemplate.query(query, this::mapRowToInfoTugasAkhir, nidPenguji, nidPenguji);
    // }

    @Override
    public List<Map<String, Object>> findSidangByPengujiWithNames(String nidPenguji) {
        String query = "SELECT i.id_ta, i.nim, m.nama AS namaMahasiswa, i.judul, i.tempat, i.tanggal, i.waktu, i.jenista, " +
                    "d1.nama AS namaPembimbing1, d2.nama AS namaPembimbing2, " +
                    "d3.nama AS namaPenguji1, d4.nama AS namaPenguji2 " +
                    "FROM InformasiTugasAkhir i " +
                    "LEFT JOIN Mahasiswa m ON i.nim = m.nim " +
                    "LEFT JOIN Dosen d1 ON i.nid_pembimbing1 = d1.nid " +
                    "LEFT JOIN Dosen d2 ON i.nid_pembimbing2 = d2.nid " +
                    "LEFT JOIN Dosen d3 ON i.nid_penguji1 = d3.nid " +
                    "LEFT JOIN Dosen d4 ON i.nid_penguji2 = d4.nid " +
                    "WHERE i.nid_penguji1 = ? OR i.nid_penguji2 = ?";
        return jdbcTemplate.queryForList(query, nidPenguji, nidPenguji);
    }

    @Override
    public List<Map<String, Object>> findSidangByPembimbingWithNames(String nidPembimbing) {
        String query = "SELECT i.id_ta, i.nim, m.nama AS namaMahasiswa, i.judul, i.tempat, i.tanggal, i.waktu, i.jenista, " +
                    "d1.nama AS namaPembimbing1, d2.nama AS namaPembimbing2, " +
                    "d3.nama AS namaPenguji1, d4.nama AS namaPenguji2 " +
                    "FROM InformasiTugasAkhir i " +
                    "LEFT JOIN Mahasiswa m ON i.nim = m.nim " +
                    "LEFT JOIN Dosen d1 ON i.nid_pembimbing1 = d1.nid " +
                    "LEFT JOIN Dosen d2 ON i.nid_pembimbing2 = d2.nid " +
                    "LEFT JOIN Dosen d3 ON i.nid_penguji1 = d3.nid " +
                    "LEFT JOIN Dosen d4 ON i.nid_penguji2 = d4.nid " +
                    "WHERE i.nid_pembimbing1 = ? OR i.nid_pembimbing2 = ?";
        return jdbcTemplate.queryForList(query, nidPembimbing, nidPembimbing);
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

    private NpmMahasiswaPair mapRowToNpmMahasiswaPair(ResultSet rs, int rowNum) throws SQLException {
        return new NpmMahasiswaPair(
                rs.getString("nim"),
                rs.getString("nama")
        );
    }


    @Override
    public int findIdTaByNpm(String npm) {
        String query = "SELECT id_ta FROM InformasiTugasAkhir WHERE nim = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, npm);
    }

    @Override
    public int findIdKomponenNilaiByDeskripsi(String deskripsi) {
        String query = "SELECT id_nilai FROM KomponenNilai WHERE komponen = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, deskripsi);
    }

    @Override
    public InfoTugasAkhir findByNpm(String npm) {
        String query = "SELECT * FROM informasitugasakhir WHERE nim = ?";
        return jdbcTemplate.queryForObject(query, this::mapRowToInfoTugasAkhir, npm);
    }


}
