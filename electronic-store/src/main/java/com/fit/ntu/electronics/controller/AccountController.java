package com.fit.ntu.electronics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class AccountController {

    @GetMapping
    public String viewProfile(Model model) {
        // Truyền tạm thông tin người dùng mẫu ra view
        model.addAttribute("firstName", "Md");
        model.addAttribute("lastName", "Rimel");
        model.addAttribute("email", "rimel1111@gmail.com");
        model.addAttribute("address", "Kingston, 5236, United State");
        
        return "profile";
    }
}