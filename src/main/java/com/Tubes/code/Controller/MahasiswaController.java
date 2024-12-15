package com.Tubes.code.Controller;

import com.Tubes.code.Entity.CatatanSidang;
import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.Mahasiswa;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Service.DosenService;
import com.Tubes.code.Service.InfoTugasAkhirService;
import com.Tubes.code.Service.MahasiswaService;
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
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    InfoTugasAkhirService infoTugasAkhirService;

    @Autowired
    PenilaianDetailService penilaianDetailService;

    @Autowired
    private CatatanSidangRepository catatanSidangRepository;

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

    @GetMapping("/nilai")
    public String getNilai(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "mahasiswa/nilai";
    }

    @GetMapping("/catatan-ta")
    public String getCatatanTa(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
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

}
