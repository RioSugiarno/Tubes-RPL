package com.Tubes.code.Controller;

import com.Tubes.code.Entity.User;
import com.Tubes.code.Service.MahasiswaService;
import com.Tubes.code.Service.UserService;
import com.Tubes.code.Service.DosenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    MahasiswaService mahasiswaService;

    @Autowired
    UserService userService;

    @Autowired
    DosenService dosenService;

    // Kalo akses localhost:8080, langsung ke halaman login.
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //Coba lagi bagian ini
    // @PostMapping("/login")
    // public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) throws Exception {
    //     User user = userService.login(username, password);
    //     if (user != null) {
    //         // Simpan NID jika role adalah Penguji
    //         if ("Penguji".equals(user.getRole())) {
    //             String nid = dosenService.getNID(user.getNamaLengkap()); // Ambil NID berdasarkan nama penguji
    //             if (nid == null) {
    //                 model.addAttribute("error", "NID tidak ditemukan untuk penguji.");
    //                 return "login";
    //             }
    //             session.setAttribute("loggedInUser", nid); // Simpan NID di session
    //         } else if ("Pembimbing".equals(user.getRole())) {
    //             String nid = dosenService.getNID(user.getNamaLengkap()); // Ambil NID berdasarkan nama pembimbing
    //             if (nid == null) {
    //                 model.addAttribute("error", "NID tidak ditemukan untuk pembimbing.");
    //                 return "login";
    //             }
    //             session.setAttribute("loggedInUser", nid); // Simpan NID di session
    //         } else {
    //             session.setAttribute("loggedInUser", user.getNamaLengkap()); // Simpan nama lengkap untuk role lain
    //         }
    //         return switch (user.getRole()) {
    //             case "Mahasiswa" -> "redirect:/mahasiswa/homescreen";
    //             case "Koordinator" -> "redirect:/koordinator/homescreen";
    //             case "Penguji" -> "redirect:/penguji/homescreen";
    //             case "Pembimbing" -> "redirect:/pembimbing/homescreen";
    //             default -> "redirect:/login";
    //         };
    //     } else {
    //         model.addAttribute("error", "Invalid username or password");
    //         return "login";
    //     }
    // }

    // Udah Jalan untuk Penguji, Pembimbing, Koordinator
    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) throws Exception {
        User user = userService.login(username, password);
        if (user != null) {
            switch (user.getRole()) {
                case "Mahasiswa" -> {
                    // Cari NIM Mahasiswa berdasarkan username
                    String nim = mahasiswaService.findNIMByUsername(username);
                    if (nim == null) {
                        model.addAttribute("error", "NIM tidak ditemukan untuk mahasiswa.");
                        return "login";
                    }
                    session.setAttribute("loggedInUser", nim);
                    return "redirect:/mahasiswa/homescreen";
                }
                case "Koordinator" -> {
                    String nidKoordinator = dosenService.getNID(user.getNamaLengkap());
                    if (nidKoordinator == null) {
                        model.addAttribute("error", "NID tidak ditemukan untuk koordinator.");
                        return "login";
                    }
                    session.setAttribute("loggedInUser", nidKoordinator);
                    return "redirect:/koordinator/homescreen";
                }
                case "Penguji" -> {
                    String nidPenguji = dosenService.getNID(user.getNamaLengkap());
                    if (nidPenguji == null) {
                        model.addAttribute("error", "NID tidak ditemukan untuk penguji.");
                        return "login";
                    }
                    session.setAttribute("loggedInUser", nidPenguji);
                    return "redirect:/penguji/homescreen";
                }
                case "Pembimbing" -> {
                    String nidPembimbing = dosenService.getNID(user.getNamaLengkap());
                    if (nidPembimbing == null) {
                        model.addAttribute("error", "NID tidak ditemukan untuk pembimbing.");
                        return "login";
                    }
                    session.setAttribute("loggedInUser", nidPembimbing);
                    return "redirect:/pembimbing/homescreen";
                }
                default -> {
                    model.addAttribute("error", "Role tidak valid.");
                    return "login";
                }
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
