package com.Tubes.code.Service;

import com.Tubes.code.Entity.NilaiTotal;
import com.Tubes.code.Entity.PenilaianDetail;
import com.Tubes.code.Repository.ImplNilaiTotalRepository;
import com.Tubes.code.Repository.InfoTugasAkhirRepository;
import com.Tubes.code.Repository.KomponenNilaiRepository;
import com.Tubes.code.Repository.NilaiTotalRepository;
import com.Tubes.code.Repository.PenilaianDetailRepository;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class PenilaianDetailService {
    private static final Logger log = LoggerFactory.getLogger(PenilaianDetailService.class);

    @Autowired
    private InfoTugasAkhirRepository infoTugasAkhirRepository;
    
    @Autowired
    private PenilaianDetailRepository penilaianDetailRepository;

    @Autowired
    private NilaiTotalRepository nilaiTotalRepository;

    @Autowired
    private KomponenNilaiRepository komponenNilaiRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isAlreadyEvaluated(int idTa, String nidPenilai) {
        String query = "SELECT COUNT(*) FROM PenilaianDetail WHERE ID_TA = ? AND NID_Penilai = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, idTa, nidPenilai);
        return count != null && count > 0;
    }

    @Transactional
    public void saveAllPenilaianDetails(int idTa, String nidPenilai, Map<Integer, Double> nilaiMap) {
        String query = "INSERT INTO PenilaianDetail (ID_TA, ID_Nilai, NID_Penilai, Nilai) VALUES (?, ?, ?, ?)";
        nilaiMap.forEach((idNilai, nilai) -> {
            jdbcTemplate.update(query, idTa, idNilai, nidPenilai, nilai);
        });
    }

    public void updateTotalScore(int idTa) {
        log.info("Memulai update total score untuk ID_TA: {}", idTa);
    
        // Ambil semua detail penilaian berdasarkan ID_TA
        List<PenilaianDetail> penilaianDetails = penilaianDetailRepository.findByIdTa(idTa);
        if (penilaianDetails.isEmpty()) {
            log.warn("Tidak ada detail penilaian ditemukan untuk ID_TA: {}", idTa);
            return;
        }
        log.info("Jumlah detail penilaian yang ditemukan: {}", penilaianDetails.size());
    
        // Hitung total dengan bobot
        double totalScore = 0.0;
        int jumlahEvaluator = 0;
        for (PenilaianDetail detail : penilaianDetails) {
            log.info("Memproses detail penilaian: {}", detail);
    
            BigDecimal bobot = komponenNilaiRepository.findBobotByIdNilai(detail.getIdNilai());
            if (bobot == null) {
                log.warn("Bobot tidak ditemukan untuk ID_Nilai: {}", detail.getIdNilai());
                continue;
            }
            log.info("Bobot ditemukan untuk ID_Nilai {}: {}", detail.getIdNilai(), bobot);
    
            totalScore += detail.getNilai().doubleValue() * (bobot.doubleValue() / 100);
            jumlahEvaluator++;
        }
    
        if (jumlahEvaluator > 0) {
            totalScore = totalScore / jumlahEvaluator;
            log.info("Total score setelah pembagian dengan evaluator: {}", totalScore);
        } else {
            log.warn("Tidak ada evaluator valid untuk ID_TA: {}", idTa);
            return;
        }
    
        // Simpan ke NilaiTotal
        NilaiTotal nilaiTotal = new NilaiTotal();
        nilaiTotal.setIdTa(idTa);
        nilaiTotal.setTotal(BigDecimal.valueOf(totalScore));
        nilaiTotal.setLastUpdated(LocalDateTime.now());
        log.info("Sebelum menyimpan data NilaiTotal: {}", nilaiTotal);
    
        nilaiTotalRepository.saveOrUpdate(nilaiTotal);
    
        log.info("Data NilaiTotal berhasil disimpan untuk ID_TA: {}", idTa);
    }

    // Udah jalan tapi Penilai NID
    // public Map<String, Map<String, Double>> getGroupedPenilaianDetailsByIdTa(int idTa) {
    //     List<PenilaianDetail> penilaianDetails = penilaianDetailRepository.findByIdTa(idTa);
    
    //     Map<String, Map<String, Double>> groupedDetails = new HashMap<>();
    //     for (PenilaianDetail detail : penilaianDetails) {
    //         String nidPenilai = detail.getNidPenilai();
    //         String komponen = komponenNilaiRepository.findKomponenById(detail.getIdNilai());
            
    //         if (komponen == null) {
    //             log.warn("Komponen tidak ditemukan untuk ID_Nilai: {}", detail.getIdNilai());
    //             continue;
    //         }
    
    //         groupedDetails.putIfAbsent(nidPenilai, new HashMap<>());
    //         groupedDetails.get(nidPenilai).put(komponen, detail.getNilai().doubleValue());
    //     }
    
    //     return groupedDetails;
    // }

    public Map<String, Map<String, Double>> getGroupedPenilaianDetailsByIdTaWithNames(int idTa) {
        List<PenilaianDetail> penilaianDetails = penilaianDetailRepository.findByIdTa(idTa);
    
        Map<String, Map<String, Double>> groupedDetails = new HashMap<>();
        for (PenilaianDetail detail : penilaianDetails) {
            String nidPenilai = detail.getNidPenilai();
            String namaPenilai = infoTugasAkhirRepository.findNamaDosenByNID(nidPenilai); // Dapatkan nama penilai
            String komponen = komponenNilaiRepository.findKomponenById(detail.getIdNilai());
    
            if (komponen == null || namaPenilai == null) continue;
    
            groupedDetails.putIfAbsent(namaPenilai, new HashMap<>());
            groupedDetails.get(namaPenilai).put(komponen, detail.getNilai().doubleValue());
        }
    
        return groupedDetails;
    }
}
