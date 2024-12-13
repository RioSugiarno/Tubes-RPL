package com.Tubes.code.Controller;

import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Service.DosenService;
import com.Tubes.code.Service.InfoTugasAkhirService;
import com.Tubes.code.Service.PenilaianDetailService;

import jakarta.servlet.http.HttpSession;
import com.Tubes.code.Entity.PenilaianDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/penguji")
public class PengujiController {

    @Autowired
    DosenService dosenService;

    @Autowired
    InfoTugasAkhirService infoTugasAkhirService;

    @Autowired
    PenilaianDetailService penilaianDetailService;


    @GetMapping("/homescreen")
    public String homescreen(Model model, HttpSession session) {
        String penguji = (String) session.getAttribute("loggedInUser");
        model.addAttribute("penguji", penguji);
        return "penguji/penguji-homescreen";
    }

    // Penguji = informasi sidang
    @GetMapping("/informasi-sidang")
    public String getInformasiSidang(Model model, HttpSession session) {
        String nidPenguji = (String) session.getAttribute("loggedInUser"); // NID dari session
        if (nidPenguji == null) {
            return "redirect:/login"; // Pastikan user sudah login
        }

        List<Map<String, Object>> informasiSidang = infoTugasAkhirService.getSidangByPengujiWithNames(nidPenguji);
        model.addAttribute("sidangList", informasiSidang);
        return "penguji/informasi-sidang";
    }

    // Penguji = list drop down mahasiswa di input-nilai-sidang
    @GetMapping("/input-nilai-sidang")
    public String getInputNilaiSidang(Model model) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "penguji/input-nilai-sidang";
    }

    @PostMapping("/input-nilai-sidang")
    @ResponseBody
    public Map<String, String> postInputNilaiSidang(
            @RequestParam("npm") String npm,
            @RequestParam Map<String, String> nilaiInputs,
            HttpSession session) {

        Map<String, String> response = new HashMap<>();
        String nidPenguji = (String) session.getAttribute("loggedInUser");

        if (nidPenguji == null) {
            response.put("status", "error");
            response.put("message", "User tidak terautentikasi.");
            return response;
        }

        try {
            int idTa = infoTugasAkhirService.getIdTaByNpm(npm);

            // Validasi apakah sudah dinilai
            if (penilaianDetailService.isAlreadyEvaluated(idTa, nidPenguji)) {
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

            penilaianDetailService.saveAllPenilaianDetails(idTa, nidPenguji, nilaiMap);

            response.put("status", "success");
            response.put("message", "Data berhasil disimpan.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Terjadi kesalahan saat menyimpan data: " + e.getMessage());
        }

        return response;
    }

    // Penguji = list drop down mahasiswa di bap
    @GetMapping("/bap")
    public String getBap(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "penguji/bap";
    }

    @PostMapping("/bap")
    public String inputBAP(Model model, HttpSession session) {
        // Implement the logic for BAP input here
        return "";
    }
}
