package com.Tubes.code.Controller;

import com.Tubes.code.Entity.NpmMahasiswaPair;
import com.Tubes.code.Service.InfoTugasAkhirService;
import com.Tubes.code.Service.KomponenNilaiService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/koordinator")
public class KoordinatorController {
    // @Autowired
    // private KomponenNilaiService komponenNilaiService;
    @Autowired
    private InfoTugasAkhirService infoTugasAkhirService;

    @GetMapping("/homescreen")
    public String homescreen(Model model, HttpSession session) {
        String koordinator = (String) session.getAttribute("loggedInUser");
        model.addAttribute("koordinator", koordinator);
        return "koordinator-homescreen";
    }

    @GetMapping("/input-data-mahasiswa")
    public String getInputDataMahasiswa(Model model, HttpSession session) {
        return "koordinator-input-data-mahasiswa";
    }

    @GetMapping("/input-jadwal-sidang-mahasiswa")
    public String getInputJadwalSidangMahasiswa(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent()&&!pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "koordinator-input-jadwal-mahasiswa";
    }

    @GetMapping("/bap")
    public String getBap(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent()&&!pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "koordinator-bap";
    }

    @GetMapping("/komponen-nilai")
    public String getKomponenNilai(Model model, HttpSession session) {
        return "koordinator-komponen-nilai";
    }

    @PostMapping("/input-data-mahasiswa")
    public String inputDataMahasiswa(@RequestParam String npm, @RequestParam String judul, @RequestParam String jenis,
                                     @RequestParam String pembimbing1, @RequestParam String pembimbing2, Model model, HttpSession session) {
        if (!infoTugasAkhirService.addDataMahasiswa(npm, judul, jenis, pembimbing1, pembimbing2)){
            model.addAttribute("error", "Data tidak berhasil diinput");
            return "koordinator-input-data-mahasiswa";
        }
        return "redirect:/koordinator/input-data-mahasiswa";
    }

    @PostMapping("/input-jadwal-mahasiswa")
    public String inputJadwalMahasiswa(@RequestParam String npm, @RequestParam LocalDate jadwal, @RequestParam LocalTime waktu, @RequestParam String tempat,
                                       @RequestParam String penguji1, @RequestParam String penguji2, Model model, HttpSession session) {
        if (!infoTugasAkhirService.addJadwalMahasiswa(npm,jadwal,waktu,tempat,penguji1,penguji2)){
            model.addAttribute("error", "Data tidak berhasil diinput");
            return "redirect:/koordinator/input-jadwal-mahasiswa";
        }
        return "koordinator-input-jadwal-mahasiswa";
    }

    @PostMapping("/bap")
    public String inputBAP(Model model, HttpSession session) {

        return "";
    }
}
