package com.Tubes.code.Controller;

import com.Tubes.code.Entity.CatatanSidang;
import com.Tubes.code.Entity.Dosen;
import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.Mahasiswa;
import com.Tubes.code.Entity.NilaiTotal;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Service.CatatanTAService;
import com.Tubes.code.Service.DosenService;
import com.Tubes.code.Service.InfoTugasAkhirService;
import com.Tubes.code.Service.MahasiswaService;
import com.Tubes.code.Service.NilaiTotalService;
import com.Tubes.code.Service.PenilaianDetailService;

import jakarta.servlet.http.HttpSession;
import com.Tubes.code.Entity.PenilaianDetail;
import com.Tubes.code.Repository.CatatanSidangRepository;
import com.Tubes.code.Repository.InfoTugasAkhirRepository;
import com.Tubes.code.Repository.NilaiTotalRepository;
import com.Tubes.code.Repository.PenilaianDetailRepository;

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
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private InfoTugasAkhirRepository infoTugasAkhirRepository;

    @Autowired
    private NilaiTotalRepository nilaiTotalRepository;

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    InfoTugasAkhirService infoTugasAkhirService;

    @Autowired
    PenilaianDetailService penilaianDetailService;

    @Autowired
    private CatatanSidangRepository catatanSidangRepository;

    @Autowired
    private PenilaianDetailRepository penilaianDetailRepository;

    @Autowired
    private CatatanTAService catatanTAService;

    @Autowired
    private NilaiTotalService nilaiTotalService;  // Autowired NilaiTotalService


    @Autowired
    private DosenService dosenService;

    @GetMapping("/homescreen")
    public String homescreen(HttpSession session, Model model) {
        String nimMahasiswa = (String) session.getAttribute("loggedInUser");
        if (nimMahasiswa == null) {
            return "redirect:/login"; // Jika sesi kosong, redirect ke halaman login
        }

        // Cari mahasiswa berdasarkan NIM
        Mahasiswa mahasiswa = mahasiswaService.findByNim(nimMahasiswa);
        if (mahasiswa != null) {
            // Menambahkan nama mahasiswa ke model
            model.addAttribute("mahasiswa", mahasiswa.getNama());
        } else {
            model.addAttribute("error", "Mahasiswa tidak ditemukan.");
        }

        return "mahasiswa/mahasiswa-homescreen"; // Redirect ke homescreen
    }

    @GetMapping("/informasi-sidang")
    public String getSidangByMahasiswa(HttpSession session, Model model) {
        // Mengambil nimMahasiswa dari session
        String nimMahasiswa = (String) session.getAttribute("loggedInUser");
        System.out.println("NIM Mahasiswa dari sesi: " + nimMahasiswa);

        if (nimMahasiswa == null) {
            return "redirect:/login";
        }

        List<Map<String, Object>> sidangList = infoTugasAkhirService
                .getSidangByMahasiswaWithNames(nimMahasiswa);
        model.addAttribute("sidangList", sidangList);
        return "mahasiswa/informasi-sidang";
    }

    

    @GetMapping("/catatan-ta")
    public String getCatatanTa(HttpSession session, Model model) {
        String nimMahasiswa = (String) session.getAttribute("loggedInUser");
        if (nimMahasiswa == null) {
            return "redirect:/login"; // Redirect jika sesi kosong
        }

        // Ambil daftar pembimbing
        List<Dosen> pembimbingList = dosenService.getPembimbingByMahasiswa(nimMahasiswa);
        model.addAttribute("pembimbingList", pembimbingList); // Kirim daftar pembimbing ke template

        if (!pembimbingList.isEmpty()) {
            Dosen pembimbing = pembimbingList.get(0); // Ambil pembimbing pertama untuk contoh
            Map<String, String> catatan = catatanTAService.getCatatanByMahasiswaAndPembimbing(
                    nimMahasiswa, pembimbing.getNID());
            model.addAttribute("catatan", catatan); // Kirim catatan ke template
        } else {
            model.addAttribute("message", "Tidak ada pembimbing tersedia."); // Tambahkan pesan jika tidak ada pembimbing
        }

        return "mahasiswa/catatan-ta";
    }

    @GetMapping("/bap")
    public String getBAP(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "mahasiswa/bap";
    }

    @PostMapping("/login")
    public String login(@RequestParam("nim") String nim,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        // Verifikasi kredensial mahasiswa
        Mahasiswa mahasiswa = mahasiswaService.authenticate(nim, password);
        if (mahasiswa != null) {
            // Simpan NIM mahasiswa di session
            session.setAttribute("loggedInUser", mahasiswa.getNIM());
            return "redirect:/mahasiswa/homescreen"; // Redirect ke halaman homescreen
        } else {
            model.addAttribute("error", "NIM atau Password salah.");
            return "login"; // Kembali ke halaman login
        }
    }

    // Nilai TA

    // @GetMapping("/nilai")
    // public String getNilai(Model model, HttpSession session) {
    //     Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
    //     if (pair.isPresent() && !pair.get().isEmpty()) {
    //         model.addAttribute("pair", pair.get());
    //     }
    //     return "mahasiswa/nilai";
    // }
 

    // @GetMapping("/nilai")
    // public String getNilaiMahasiswa(HttpSession session, Model model) {
    //     String nim = (String) session.getAttribute("loggedInUser");
    //     if (nim == null) {
    //         return "redirect:/login";  // Jika sesi kosong, redirect ke halaman login
    //     }
    
    //     // Mencari ID_TA berdasarkan NIM Mahasiswa
    //     Integer idTa = infoTugasAkhirService.findIdTaByNim(nim);
    //     if (idTa == null) {
    //         model.addAttribute("totalScore", "Nilai Total belum Tersedia!");
    //         return "mahasiswa/nilai";  // Jika ID_TA tidak ditemukan, tampilkan pesan "Nilai Total belum Tersedia!"
    //     }
    
    //     // Mengambil nilai total menggunakan service
    //     BigDecimal totalScore = nilaiTotalService.getTotalNilaiByIdTa(idTa);
    
    //     if (totalScore.compareTo(BigDecimal.ZERO) == 0) {
    //         model.addAttribute("totalScore", "Nilai Total belum Tersedia!");
    //     } else {
    //         model.addAttribute("totalScore", totalScore);  // Mengirimkan total score ke view
    //     }
    
    //     // Mengambil data penilaian details menggunakan service
    //     List<PenilaianDetail> penilaianDetails = penilaianDetailService.findByIdTa(idTa);  // Ubah ke metode yang benar
    //     model.addAttribute("penilaianDetails", penilaianDetails);  // Mengirimkan penilaianDetails ke view
    
    //     return "mahasiswa/nilai";  // Menampilkan halaman nilai
    // }

    @GetMapping("/nilai")
    public String getNilaiMahasiswa(HttpSession session, Model model) {
        String nim = (String) session.getAttribute("loggedInUser");
        if (nim == null) {
            return "redirect:/login";
        }

        Integer idTa = infoTugasAkhirService.findIdTaByNim(nim);
        if (idTa == null) {
            model.addAttribute("totalScore", "Nilai Total belum Tersedia!");
            return "mahasiswa/nilai";
        }

        BigDecimal totalScore = nilaiTotalService.getTotalNilaiByIdTa(idTa);
        model.addAttribute("totalScore", totalScore.compareTo(BigDecimal.ZERO) == 0 ? "Nilai Total belum Tersedia!" : totalScore);

        Map<String, Map<String, Double>> groupedDetails = penilaianDetailService.getGroupedPenilaianDetailsByIdTaWithNames(idTa);
        model.addAttribute("groupedDetails", groupedDetails);

        return "mahasiswa/nilai";
    }
}
