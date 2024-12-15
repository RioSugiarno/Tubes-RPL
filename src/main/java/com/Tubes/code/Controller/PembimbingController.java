package com.Tubes.code.Controller;

import com.Tubes.code.Entity.CatatanSidang;
import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Service.DosenService;
import com.Tubes.code.Service.InfoTugasAkhirService;
import com.Tubes.code.Service.PenilaianDetailService;

import jakarta.servlet.http.HttpSession;
import com.Tubes.code.Entity.PenilaianDetail;
import com.Tubes.code.Repository.CatatanSidangRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/pembimbing")
public class PembimbingController {

    @Autowired
    DosenService dosenService;

    @Autowired
    InfoTugasAkhirService infoTugasAkhirService;

    @Autowired
    PenilaianDetailService penilaianDetailService;

    @Autowired
    private CatatanSidangRepository catatanSidangRepository;

    @GetMapping("/homescreen")
    public String homescreen(Model model, HttpSession session) {
        String pembimbing = (String) session.getAttribute("loggedInUser");
        model.addAttribute("pembimbing", pembimbing);
        return "pembimbing/pembimbing-homescreen";
    }

    // Pembimbing = informasi sidang
    @GetMapping("/informasi-sidang")
    public String getInformasiSidang(Model model, HttpSession session) {
        String nidPembimbing = (String) session.getAttribute("loggedInUser"); // NID dari session
        if (nidPembimbing == null) {
            return "redirect:/login"; // Pastikan user sudah login
        }

        List<Map<String, Object>> informasiSidang = infoTugasAkhirService.getSidangByPembimbingWithNames(nidPembimbing);
        model.addAttribute("sidangList", informasiSidang);
        return "pembimbing/informasi-sidang";
    }

    // Pembimbing = list drop down mahasiswa di input-nilai-sidang
    @GetMapping("/input-nilai-sidang")
    public String getInputNilaiSidang(Model model) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "pembimbing/input-nilai-sidang";
    }

    @PostMapping("/input-nilai-sidang")
    @ResponseBody
    public Map<String, String> postInputNilaiSidang(
            @RequestParam("npm") String npm,
            @RequestParam Map<String, String> nilaiInputs,
            HttpSession session) {

        Map<String, String> response = new HashMap<>();
        String nidPembimbing = (String) session.getAttribute("loggedInUser");

        if (nidPembimbing == null) {
            response.put("status", "error");
            response.put("message", "User tidak terautentikasi.");
            return response;
        }

        try {
            int idTa = infoTugasAkhirService.getIdTaByNpm(npm);

            // Validasi apakah sudah dinilai
            if (penilaianDetailService.isAlreadyEvaluated(idTa, nidPembimbing)) {
                response.put("status", "error");
                response.put("message", "Nilai untuk tugas akhir ini sudah diberikan oleh Anda.");
                return response;
            }

            Map<Integer, Double> nilaiMap = new HashMap<>();
            nilaiInputs.forEach((key, value) -> {
                if (key.startsWith("komponen_")) {
                    Integer idKomponen = Integer.parseInt(key.replace("komponen_", ""));
                    Double nilai = Double.parseDouble(value);
                    nilaiMap.put(idKomponen, nilai);
                }
            });

            penilaianDetailService.saveAllPenilaianDetails(idTa, nidPembimbing, nilaiMap);

            response.put("status", "success");
            response.put("message", "Data berhasil disimpan.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Terjadi kesalahan saat menyimpan data: " + e.getMessage());
        }

        return response;
    }

    // Pembimbing = list drop down mahasiswa di bap
    @GetMapping("/bap")
    public String getBap(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent()&&!pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "pembimbing/bap";
    }

    // Catatan TA
    @GetMapping("/catatan-ta")
    public String getCatatanTa(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent()&&!pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "pembimbing/catatan-ta";
    }

    // Udah Jalan
    // @GetMapping("/get-info-ta")
    // public ResponseEntity<?> getInfoTugasAkhir(@RequestParam("npm") String npm) {
    //     try {
    //         InfoTugasAkhir infoTugasAkhir = infoTugasAkhirService.findByNpm(npm);

    //         if (infoTugasAkhir == null) {
    //             return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Data tidak ditemukan untuk NPM: " + npm));
    //         }

    //         System.out.println("InfoTugasAkhir ditemukan: " + infoTugasAkhir);

    //         Map<String, Object> response = Map.of(
    //             "idTa", infoTugasAkhir.getIdTa(),
    //             "nidPenguji", infoTugasAkhir.getNid_penguji1() // Sesuaikan penguji
    //         );
    //         return ResponseEntity.ok(response);

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(500).body(Map.of("error", true, "message", "Terjadi kesalahan saat mengambil data: " + e.getMessage()));
    //     }
    // }

    @GetMapping("/get-info-ta")
    public ResponseEntity<?> getInfoTugasAkhir(@RequestParam("npm") String npm, HttpSession session) {
        try {
            InfoTugasAkhir infoTugasAkhir = infoTugasAkhirService.findByNpm(npm);

            if (infoTugasAkhir == null) {
                return ResponseEntity.badRequest().body(Map.of("error", true, "message", "Data tidak ditemukan untuk NPM: " + npm));
            }

            System.out.println("InfoTugasAkhir ditemukan: " + infoTugasAkhir);

            String loggedInNid = (String) session.getAttribute("loggedInUser");
            if (loggedInNid == null) {
                return ResponseEntity.badRequest().body("User tidak terautentikasi.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("idTa", infoTugasAkhir.getIdTa());
            response.put("npm", infoTugasAkhir.getNpm());
            response.put("nidPembimbing1", infoTugasAkhir.getNid_pembimbing1());
            response.put("nidPembimbing2", infoTugasAkhir.getNid_pembimbing2());
            response.put("loggedInNid", loggedInNid); 

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", true, "message", "Terjadi kesalahan saat mengambil data: " + e.getMessage()));
        }
    }

    // @PostMapping("/catatan-ta")
    // public ResponseEntity<?> submitCatatanSidang(@RequestBody CatatanSidang catatan) {
    //     try {
    //         // Simpan catatan ke database
    //         int rowsAffected = catatanSidangRepository.save(catatan);

    //         if (rowsAffected > 0) {
    //             return ResponseEntity.ok("Catatan sidang berhasil disimpan.");
    //         } else {
    //             return ResponseEntity.status(500).body("Gagal menyimpan catatan.");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(500).body("Terjadi kesalahan saat menyimpan catatan.");
    //     }
    // }

    // @PostMapping("/catatan-ta")
    // public ResponseEntity<?> submitCatatanSidang(@RequestBody CatatanSidang catatan) {
    //     try {
    //         System.out.println("Payload diterima: " + catatan);
            
    //         if (catatan.getIdTa() == null || catatan.getCatatan() == null ||
    //             catatan.getNidMahasiswa() == null || catatan.getNidPenguji() == null) {
    //             return ResponseEntity.badRequest().body("Semua field harus diisi.");
    //         }

    //         // Simpan catatan ke database
    //         int rowsAffected = catatanSidangRepository.save(catatan);

    //         if (rowsAffected > 0) {
    //             return ResponseEntity.ok("Catatan sidang berhasil disimpan.");
    //         } else {
    //             return ResponseEntity.status(500).body("Gagal menyimpan catatan.");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(500).body("Terjadi kesalahan saat menyimpan catatan: " + e.getMessage());
    //     }
    // }

    // Database Catatan TA Baru - Catatan 1 Bisa
    @PostMapping("/catatan-ta")
    public ResponseEntity<?> submitCatatanSidang(@RequestBody CatatanSidang catatan, HttpSession session) {
        try {
            String loggedInNid = (String) session.getAttribute("loggedInUser");
            if (loggedInNid == null) {
                return ResponseEntity.badRequest().body("User tidak terautentikasi.");
            }

            System.out.println("Payload dari frontend: " + catatan);

            InfoTugasAkhir info = infoTugasAkhirService.findByNpm(catatan.getNidMahasiswa());
            if (info == null) {
                return ResponseEntity.badRequest().body("Data tugas akhir tidak ditemukan.");
            }

            // Tentukan kolom yang akan diperbarui berdasarkan pembimbing login
            String updateCatatan = null;
            if (loggedInNid.equals(info.getNid_pembimbing1())) {
                updateCatatan = catatan.getCatatan1();
            } else if (loggedInNid.equals(info.getNid_pembimbing2())) {
                updateCatatan = catatan.getCatatan2();
            }

            if (updateCatatan == null) {
                return ResponseEntity.badRequest().body("Anda tidak memiliki akses untuk menyimpan catatan ini.");
            }

            catatanSidangRepository.insertOrUpdateCatatanTa(
                info.getIdTa(),
                info.getNid_pembimbing1(),
                info.getNid_pembimbing2(),
                info.getNpm(),
                updateCatatan,
                loggedInNid
            );

            return ResponseEntity.ok("Catatan berhasil disimpan.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Terjadi kesalahan: " + e.getMessage());
        }
    }
}
