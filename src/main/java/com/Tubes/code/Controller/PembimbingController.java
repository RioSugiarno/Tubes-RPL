package com.Tubes.code.Controller;

import com.Tubes.code.Service.DosenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pembimbing")
public class PembimbingController {

    @Autowired
    DosenService dosenService;

    @GetMapping("/homescreen")
    public String homescreen(Model model, HttpSession session) {
        String pembimbing = (String) session.getAttribute("loggedInUser");
        model.addAttribute("pembimbing", pembimbing);
        return "pembimbing-homescreen";
    }
}
