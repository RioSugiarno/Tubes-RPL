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
public class ImplInfoTugasAkhirRepository implements InfoTugasAkhirRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean insertDataMahasiswa(String npm, String judul, String jenis, String pembimbing1, String pembimbing2) {
        try {
            String query = "INSERT INTO informasitugasakhir (nim, judul, jenista, nid_pembimbing1, nid_pembimbing2) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(query, npm, judul, jenis, pembimbing1, pembimbing2);
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertJadwalMahasiswa(String npm, LocalDate tanggal, LocalTime waktu, String tempat, String penguji1,
            String penguji2) {
        try {
            String query = "UPDATE informasitugasakhir SET tanggal = ?, waktu = ?, tempat = ?, nid_penguji1 = ?, nid_penguji2 = ? "
                    +
                    "WHERE nim = ? AND (tanggal IS NULL AND waktu IS NULL AND tempat IS NULL AND nid_penguji1 IS NULL AND nid_penguji2 IS NULL)";
            jdbcTemplate.update(query, tanggal, waktu, tempat, penguji1, penguji2, npm);
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<InfoTugasAkhir> getInfoTugasAkhir(String judul) {
        String query = "SELECT * FROM informasitugasakhir WHERE judul = ?";
        List<InfoTugasAkhir> result = jdbcTemplate.query(query, this::mapRowToInfoTugasAkhir, judul);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<List<NpmMahasiswaPair>> createPair() {
        String query = "SELECT m.NIM, m.Nama FROM mahasiswa m JOIN informasitugasakhir i ON m.NIM = i.NIM";
        List<NpmMahasiswaPair> result = jdbcTemplate.query(query, this::mapRowToNpmMahasiswaPair);
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    // Penguji Informasi Sidang pake NID
    // @Override
    // public List<InfoTugasAkhir> findSidangByPenguji(String nidPenguji) {
    // String query = "SELECT * FROM InformasiTugasAkhir " +
    // "WHERE nid_penguji1 = ? OR nid_penguji2 = ?";
    // return jdbcTemplate.query(query, this::mapRowToInfoTugasAkhir, nidPenguji,
    // nidPenguji);
    // }

    @Override
    public List<Map<String, Object>> findSidangByPengujiWithNames(String nidPenguji) {
        String query = "SELECT i.id_ta, i.nim, m.nama AS namaMahasiswa, i.judul, i.tempat, i.tanggal, i.waktu, i.jenista, "
                +
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
        String query = "SELECT i.id_ta, i.nim, m.nama AS namaMahasiswa, i.judul, i.tempat, i.tanggal, i.waktu, i.jenista, "
                +
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

    @Override
    public List<Map<String, Object>> findSidangByMahasiswaWithNames(String nimMahasiswa) {
        String query = "SELECT i.id_ta, i.nim, m.nama AS namaMahasiswa, i.judul, i.tempat, i.tanggal, i.waktu, i.jenista, "
                + "d1.nama AS namaPembimbing1, d2.nama AS namaPembimbing2, "
                + "d3.nama AS namaPenguji1, d4.nama AS namaPenguji2, "
                + "'Koordinator 7182201001' AS namaKoordinator " // Tambahkan kolom Koordinator di query
                + "FROM InformasiTugasAkhir i "
                + "LEFT JOIN Mahasiswa m ON i.nim = m.nim "
                + "LEFT JOIN Dosen d1 ON i.nid_pembimbing1 = d1.nid "
                + "LEFT JOIN Dosen d2 ON i.nid_pembimbing2 = d2.nid "
                + "LEFT JOIN Dosen d3 ON i.nid_penguji1 = d3.nid "
                + "LEFT JOIN Dosen d4 ON i.nid_penguji2 = d4.nid "
                + "WHERE i.nim = ?";
        return jdbcTemplate.queryForList(query, nimMahasiswa);
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
                rs.getTime("waktu").toLocalTime());
    }

    private NpmMahasiswaPair mapRowToNpmMahasiswaPair(ResultSet rs, int rowNum) throws SQLException {
        return new NpmMahasiswaPair(
                rs.getString("nim"),
                rs.getString("nama"));
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

    // BAP Otomatis isi Tabel ketika Buat Sidang TA
    @Override
    public boolean insertBap(int idTa, String nidPenguji1, String nidPenguji2, String nidPembimbing1,
            String nidPembimbing2, String nidMahasiswa, String nidKoordinator) {
        try {
            String query = "INSERT INTO bap (ID_TA, NID_Penguji_1, NID_Penguji_2, NID_Pembimbing_1, NID_Pembimbing_2, NID_Mahasiswa, NID_Koordinator, "
                    +
                    "statusPenguji, statusPembimbing, statusMahasiswa, statusKoordinator) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, FALSE, FALSE, FALSE, FALSE)";
            jdbcTemplate.update(query, idTa, nidPenguji1, nidPenguji2, nidPembimbing1, nidPembimbing2, nidMahasiswa,
                    nidKoordinator);
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting BAP: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updatePengujiInBap(int idTa, String nidPenguji1, String nidPenguji2) {
        try {
            String query = "UPDATE bap SET NID_Penguji_1 = ?, NID_Penguji_2 = ? WHERE ID_TA = ?";
            jdbcTemplate.update(query, nidPenguji1, nidPenguji2, idTa);
            return true;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error updating Penguji in BAP: " + e.getMessage(), e);
        }
    }

    // Catatan TA - Catatan Sidang 1 Bisa
    public void insertOrUpdateCatatanTa(int idTa, String nidPembimbing1, String nidPembimbing2, String npm,
            String catatan, String loggedInNid) {
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
}
