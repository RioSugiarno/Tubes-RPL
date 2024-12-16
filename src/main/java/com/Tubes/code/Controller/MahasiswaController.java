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

/**
 * Menampilkan halaman homescreen untuk mahasiswa berdasarkan sesi login.
 *
 * @param session HttpSession yang menyediakan informasi sesi login.
 * @param model Model yang digunakan untuk menyimpan atribut yang akan diteruskan ke view.
 * @return String path ke view halaman homescreen mahasiswa.
 */
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

/**
 * Mengambil dan menampilkan informasi sidang berdasarkan NIM mahasiswa dari sesi.
 *
 * @param session HttpSession yang menyediakan informasi sesi login.
 * @param model Model yang digunakan untuk menyimpan data sidang yang akan diteruskan ke view.
 * @return String path ke view halaman informasi sidang mahasiswa.
 */
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

/**
 * Menampilkan daftar nilai yang relevan untuk mahasiswa berdasarkan pasangan NPM.
 *
 * @param model Model yang digunakan untuk menyimpan data nilai mahasiswa yang akan diteruskan ke view.
 * @param session HttpSession yang menawarkan data sesi login.
 * @return String path ke view halaman nilai mahasiswa.
 */
@GetMapping("/nilai")
    public String getNilai(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "mahasiswa/nilai";
    }

/**
 * Menampilkan catatan Tugas Akhir (TA) yang relevan untuk mahasiswa.
 *
 * @param model Model yang digunakan untuk menyimpan data catatan TA mahasiswa yang akan diteruskan ke view.
 * @param session HttpSession yang menawarkan data sesi login.
 * @return String path ke view halaman catatan TA mahasiswa.
 */
@GetMapping("/catatan-ta")
    public String getCatatanTa(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "mahasiswa/catatan-ta";
    }

/**
 * Menampilkan Berita Acara Pelaksanaan (BAP) untuk mahasiswa yang sesuai.
 *
 * @param model Model yang digunakan untuk menyimpan data BAP mahasiswa yang akan diteruskan ke view.
 * @param session HttpSession menawarkan data sesi login.
 * @return String path ke view halaman BAP mahasiswa.
 */
@GetMapping("/bap")
    public String getBAP(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "mahasiswa/bap";
    }

/**
 * Melakukan proses autentikasi login mahasiswa berdasarkan NIM dan password.
 *
 * @param nim NIM yang dimasukkan oleh mahasiswa.
 * @param password Password yang dimasukkan oleh mahasiswa.
 * @param session HttpSession yang digunakan untuk menyimpan data login.
 * @param model Model yang digunakan untuk menyimpan pesan error jika login gagal.
 * @return String path ke homescreen mahasiswa jika login berhasil, atau login ulang jika gagal.
 */
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
