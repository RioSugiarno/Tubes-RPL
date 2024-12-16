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
/**
 * Controller for managing the "Koordinator" module which includes operations like handling
 * student data, schedules, BAP, and component weights.
 */
@RequestMapping("/koordinator")
public class KoordinatorController {
    // @Autowired
    // private KomponenNilaiService komponenNilaiService;
    @Autowired
    private InfoTugasAkhirService infoTugasAkhirService;

    @Autowired
    KomponenNilaiService komponenNilaiService;  

/**
 * Displays the homescreen for "Koordinator" by adding the logged-in user to the model.
 *
 * @param model   The model to populate with attributes.
 * @param session The current HTTP session to fetch logged-in user data.
 * @return The path to the Koordinator homescreen view.
 */
@GetMapping("/homescreen")
public String homescreen(Model model, HttpSession session) {
        String koordinator = (String) session.getAttribute("loggedInUser");
        model.addAttribute("koordinator", koordinator);
        return "koordinator/koordinator-homescreen";
    }

/**
 * Renders the page for inputting student data.
 *
 * @param model   The model to populate with attributes.
 * @param session The current HTTP session for potential user context.
 * @return The path to the input student data view.
 */
@GetMapping("/input-data-mahasiswa")
public String getInputDataMahasiswa(Model model, HttpSession session) {
        return "koordinator/koordinator-input-data-mahasiswa";
    }

/**
 * Presents the input schedule page and fetches student pairs for scheduling purposes.
 *
 * @param model   The model used to populate student pairs if available.
 * @param session The current HTTP session for potential user context.
 * @return The path to the input schedule view.
 */
@GetMapping("/input-jadwal-sidang-mahasiswa")
public String getInputJadwalSidangMahasiswa(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "koordinator/koordinator-input-jadwal-mahasiswa";
    }

/**
 * Loads the BAP page with student data if available.
 *
 * @param model   The model to populate with student pairs if applicable.
 * @param session The current HTTP session for user-specific context.
 * @return The path to the BAP view.
 */
@GetMapping("/bap")
public String getBap(Model model, HttpSession session) {
        Optional<List<NpmMahasiswaPair>> pair = infoTugasAkhirService.findPair();
        if (pair.isPresent() && !pair.get().isEmpty()) {
            model.addAttribute("pair", pair.get());
        }
        return "koordinator/koordinator-bap";
    }

/**
 * Directs users to the component weights management page for Koordinator.
 *
 * @param model   The model for potential view data.
 * @param session The current HTTP session for context.
 * @return The path to the component weights view.
 */
@GetMapping("/komponen-nilai")
public String getKomponenNilai(Model model, HttpSession session) {
        return "koordinator/koordinator-komponen-nilai";
    }

/**
 * Handles the submission of new student data.
 *
 * @param npm          The NPM of the student.
 * @param judul        The title of the student's work.
 * @param jenis        The type of task.
 * @param pembimbing1  The first advisor's name.
 * @param pembimbing2  The second advisor's name.
 * @param model        The model for adding error messages if required.
 * @param session      The current HTTP session for potential user context.
 * @return Redirects to the input data page or error page if the process fails.
 */
@PostMapping("/input-data-mahasiswa")
public String inputDataMahasiswa(@RequestParam String npm, @RequestParam String judul, @RequestParam String jenis,
            @RequestParam String pembimbing1, @RequestParam String pembimbing2, Model model, HttpSession session) {
        if (!infoTugasAkhirService.addDataMahasiswa(npm, judul, jenis, pembimbing1, pembimbing2)) {
            model.addAttribute("error", "Data tidak berhasil diinput");
            return "koordinator-input-data-mahasiswa";
        }
        return "redirect:/koordinator/input-data-mahasiswa";
    }

/**
 * Processes the schedule submission for a student.
 *
 * @param npm      The NPM of the student.
 * @param jadwal   The scheduled date.
 * @param waktu    The scheduled time.
 * @param tempat   The venue of the schedule.
 * @param penguji1 The name of the first examiner.
 * @param penguji2 The name of the second examiner.
 * @param model    The model for storing error information.
 * @param session  The current HTTP session for potential user context.
 * @return Redirects to the schedule page or error page if submission fails.
 */
@PostMapping("/input-jadwal-mahasiswa")
public String inputJadwalMahasiswa(@RequestParam String npm, @RequestParam LocalDate jadwal,
            @RequestParam LocalTime waktu, @RequestParam String tempat,
            @RequestParam String penguji1, @RequestParam String penguji2, Model model, HttpSession session) {
        if (!infoTugasAkhirService.addJadwalMahasiswa(npm, jadwal, waktu, tempat, penguji1, penguji2)) {
            model.addAttribute("error", "Data tidak berhasil diinput");
            return "redirect:/koordinator/input-jadwal-mahasiswa";
        }
        return "redirect:/koordinator/input-jadwal-sidang-mahasiswa";
    }

/**
 * Updates the weights for examiners and advisors across multiple components.
 *
 * @param pengujiTata          Weight for examiner's structure.
 * @param pengujiKelengkapan   Weight for examiner's completeness.
 * @param pengujiTujuan        Weight for examiner's purpose clarity.
 * @param pengujiMateri        Weight for examiner's material quality.
 * @param pengujiPresentasi    Weight for examiner's presentation skills.
 * @param pembimbingTata       Weight for advisor's structure.
 * @param pembimbingKelengkapan Weight for advisor's completeness.
 * @param pembimbingProses     Weight for advisor's process.
 * @param pembimbingMateri     Weight for advisor's material quality.
 * @return Redirects to the component weights page if the update is successful, else an error.
 */
@PostMapping("/komponen-nilai")
public String updateBobot(@RequestParam("penguji-tata") Double pengujiTata,
            @RequestParam("penguji-kelengkapan") Double pengujiKelengkapan,
            @RequestParam("penguji-tujuan") Double pengujiTujuan,
            @RequestParam("penguji-materi") Double pengujiMateri,
            @RequestParam("penguji-presentasi") Double pengujiPresentasi,
            @RequestParam("pembimbing-tata") Double pembimbingTata,
            @RequestParam("pembimbing-kelengkapan") Double pembimbingKelengkapan,
            @RequestParam("pembimbing-proses") Double pembimbingProses,
            @RequestParam("pembimbing-materi") Double pembimbingMateri) {
        try {
            // Update bobot untuk Penguji
            komponenNilaiService.setBobotById(1, pengujiTata);
            komponenNilaiService.setBobotById(2, pengujiKelengkapan);
            komponenNilaiService.setBobotById(3, pengujiTujuan);
            komponenNilaiService.setBobotById(4, pengujiMateri);
            komponenNilaiService.setBobotById(5, pengujiPresentasi);

            // Update bobot untuk Pembimbing
            komponenNilaiService.setBobotById(6, pembimbingTata);
            komponenNilaiService.setBobotById(7, pembimbingKelengkapan);
            komponenNilaiService.setBobotById(8, pembimbingProses);
            komponenNilaiService.setBobotById(9, pembimbingMateri);
            return "redirect:/koordinator/komponen-nilai";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

/**
 * Placeholder for handling the BAP submission logic.
 *
 * @param model   The model for adding context-specific attributes.
 * @param session The current HTTP session for necessary context.
 * @return Currently empty, to be implemented.
 */
@PostMapping("/bap")
public String inputBAP(Model model, HttpSession session) {
        return "";
    }
}
