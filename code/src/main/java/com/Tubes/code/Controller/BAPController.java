package com.Tubes.code.Controller;

import com.Tubes.code.Service.BapService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bap")
public class BAPController {

    @Autowired
    private BapService bapService;

    // Dah Jalan Ini
    // @GetMapping("/pembimbing/bap")
    // public String showBapPage(Model model, HttpSession session) {
    //     // Ambil NID pengguna yang login dari session
    //     String currentUserId = (String) session.getAttribute("loggedInUser");

    //     // Jika NID tidak ada di session, redirect ke halaman login
    //     if (currentUserId == null || currentUserId.isEmpty()) {
    //         return "redirect:/login";
    //     }

    //     model.addAttribute("currentUserId", currentUserId);
    //     return "bap";  // Mengembalikan halaman BAP
    // }

    @GetMapping("/pembimbing/bap")
    public String showBapPage(@RequestParam(value = "npm", required = false) String npm,
                            Model model, HttpSession session) {
        String currentUserId = (String) session.getAttribute("loggedInUser");
        if (currentUserId == null || currentUserId.isEmpty()) {
            return "redirect:/login";
        }

        Map<String, Boolean> bapStatus = new HashMap<>();
        bapStatus.put("statusPenguji", false);
        bapStatus.put("statusPembimbing", false);
        bapStatus.put("statusMahasiswa", false);
        bapStatus.put("statusKoordinator", false);

        // Jika npm sudah dipilih, update bapStatus dari database
        if (npm != null && !npm.isEmpty() && !npm.equals("List NPM Mahasiswa")) {
            Map<String, Boolean> fetchedStatus = bapService.getBapStatusByNID(currentUserId, npm);
            if (fetchedStatus != null && !fetchedStatus.isEmpty()) {
                bapStatus = fetchedStatus;
            }
        }

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("bapStatus", bapStatus);
        model.addAttribute("selectedNpm", npm);

        return "pembimbing/bap";
    }


    // Udah Jalan untuk Penguji, Pembimbing, Koordinator
    @PostMapping("/upload")
    public ResponseEntity<?> uploadBap(@RequestParam("npm") String npm, HttpSession session) {
        // Ambil currentUserId dari session
        String currentUserId = (String) session.getAttribute("loggedInUser");
        if (currentUserId == null || currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in or invalid session.");
        }

        System.out.println("Received request:");
        System.out.println("npm: " + npm);
        System.out.println("currentUserId from session: " + currentUserId);

        try {
            bapService.updateBapStatus(npm, currentUserId);
            return ResponseEntity.ok("Status BAP diperbarui.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal memperbarui status BAP.");
        }
    }

    // Tes Mahasiswa
    @PostMapping("/upload2")
    public ResponseEntity<?> uploadBap2(@RequestParam("npm") String npm, HttpSession session) {
        // Ambil currentUserId dari session
        String currentUserId = (String) session.getAttribute("loggedInUser");
        if (currentUserId == null || currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in or invalid session.");
        }

        System.out.println("Received request:");
        System.out.println("npm: " + npm);
        System.out.println("currentUserId from session: " + currentUserId);

        try {
            bapService.updateBapStatus(currentUserId, currentUserId);
            return ResponseEntity.ok("Status BAP diperbarui.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal memperbarui status BAP.");
        }
    }
}
