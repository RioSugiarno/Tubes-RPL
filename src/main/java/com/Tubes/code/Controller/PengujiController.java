package com.Tubes.code.Controller;

import com.Tubes.code.Entity.InfoTugasAkhir;
import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Service.DosenService;
import com.Tubes.code.Service.InfoTugasAkhirService;
import com.Tubes.code.Service.PenilaianDetailService;

import jakarta.servlet.http.HttpSession;
import com.Tubes.code.Entity.PenilaianDetail;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/penguji")
/**
 * PengujiController handles the flow of data and business logic related to 'Penguji',
 * including managing homescreens, displaying 'informasi-sidang', inputting scores for sessions,
 * and handling BAP (Berita Acara Pemeriksaan) functionalities.
 */
public class PengujiController {

    @Autowired
    DosenService dosenService;

    @Autowired
    InfoTugasAkhirService infoTugasAkhirService;

    @Autowired
    PenilaianDetailService penilaianDetailService;


    /**
     * Displays the home screen for the penguji user.
     * @param model Spring Model object to pass data to the view.
     * @param session HttpSession object to retrieve the logged-in user's information.
     * @return String representing the view for the penguji home screen.
     */
    @GetMapping("/homescreen")
    public String homescreen(Model model, HttpSession session) {
        String penguji = (String) session.getAttribute("loggedInUser");
        model.addAttribute("penguji", penguji);
        return "penguji/penguji-homescreen";
    }

    // Penguji = informasi sidang
    /**
     * Fetches and displays information about the thesis sessions ('sidang') for a specific penguji.
     * @param model Spring Model object to pass the list of 'sidang' to the view.
     * @param session HttpSession object to identify the logged-in penguji user.
     * @return String representing the view for displaying 'informasi-sidang'.
     */
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
    /**
     * Displays a form for inputting thesis session scores. Provides a list of
     * students paired with their respective NPM (Nomor Pokok Mahasiswa).
     * @param model Spring Model object to pass the list of student-NPM pairs for display.
     * @return String representing the view for inputting thesis session scores.
     */
    @GetMapping("/input-nilai-sidang")
    public String getInputNilaiSidang(Model model) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "penguji/input-nilai-sidang";
    }

    /**
     * Processes the submitted thesis scores for a student by a penguji.
     * Retrieves the session ID based on the student's NPM and validates the score submission.
     * @param npm String representing the student's unique NPM.
     * @param nilaiInputs Map of component IDs (keys starting with 'komponen_') and their respective scores.
     * @param session HttpSession object to retrieve the logged-in penguji user's information.
     * @return Map containing the status ('success' or 'error') and a corresponding message.
     */
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
    /**
     * Displays a form for creating or editing BAP (Berita Acara Pemeriksaan) for thesis sessions.
     * Provides a list of students paired with their respective NPM (Nomor Pokok Mahasiswa).
     * @param model Spring Model object to pass the list of student-NPM pairs for display.
     * @param session HttpSession object to retrieve the logged-in penguji user's information.
     * @return String representing the view for BAP input or editing.
     */
    @GetMapping("/bap")
    public String getBap(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent()&&!pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "penguji/bap";
    }

    /**
     * Processes the submitted data for BAP (Berita Acara Pemeriksaan).
     * This method is currently not implemented.
     * @param model Spring Model object to pass any required data to the view.
     * @param session HttpSession object to retrieve the logged-in penguji user's information.
     * @return String representing the redirection or view to indicate success/failure.
     */
    @PostMapping("/bap")
    public String inputBAP(Model model, HttpSession session) {

        return "";
    }
}