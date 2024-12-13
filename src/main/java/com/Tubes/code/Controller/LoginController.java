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

    // @PostMapping("/login")
    // public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) throws Exception {
    //     User user = userService.login(username, password);
    //     if (user!=null) {
    //         session.setAttribute("loggedInUser", user.getNamaLengkap());
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

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) throws Exception {
        User user = userService.login(username, password);
        if (user != null) {
            // Simpan NID jika role adalah Penguji
            if ("Penguji".equals(user.getRole())) {
                String nid = dosenService.getNID(user.getNamaLengkap()); // Ambil NID berdasarkan nama penguji
                if (nid == null) {
                    model.addAttribute("error", "NID tidak ditemukan untuk penguji.");
                    return "login";
                }
                session.setAttribute("loggedInUser", nid); // Simpan NID di session
            } else if ("Pembimbing".equals(user.getRole())) {
                String nid = dosenService.getNID(user.getNamaLengkap()); // Ambil NID berdasarkan nama pembimbing
                if (nid == null) {
                    model.addAttribute("error", "NID tidak ditemukan untuk pembimbing.");
                    return "login";
                }
                session.setAttribute("loggedInUser", nid); // Simpan NID di session

            } else {
                session.setAttribute("loggedInUser", user.getNamaLengkap()); // Simpan nama lengkap untuk role lain
            }
            
            return switch (user.getRole()) {
                case "Mahasiswa" -> "redirect:/mahasiswa/homescreen";
                case "Koordinator" -> "redirect:/koordinator/homescreen";
                case "Penguji" -> "redirect:/penguji/homescreen";
                case "Pembimbing" -> "redirect:/pembimbing/homescreen";
                default -> "redirect:/login";
            };
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
