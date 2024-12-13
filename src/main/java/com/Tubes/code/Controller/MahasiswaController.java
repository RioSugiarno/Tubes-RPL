package com.Tubes.code.Controller;

import com.Tubes.code.Entity.User;
import com.Tubes.code.Service.MahasiswaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping("/homescreen")
    public String homescreen(Model model, HttpSession session) {
        String mahasiswa = (String) session.getAttribute("loggedInUser");
        model.addAttribute("mahasiswa", mahasiswa);
        return "mahasiswa-homescreen";
    }
}
