package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.model.User;
import com.fit.ntu.electronics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String viewProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                @RequestParam(value = "currentPassword", required = false) String currentPassword,
                                @RequestParam(value = "newPassword", required = false) String newPassword,
                                @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                                HttpSession session,
                                Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setAddress(updatedUser.getAddress());

        if (currentPassword != null && !currentPassword.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
            if (!currentUser.getPassword().equals(currentPassword)) {
                model.addAttribute("user", currentUser);
                model.addAttribute("error", "Mật khẩu hiện tại không chính xác");
                return "profile";
            }
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("user", currentUser);
                model.addAttribute("error", "Mật khẩu mới không trùng khớp");
                return "profile";
            }
            currentUser.setPassword(newPassword);
        }

        userRepository.save(currentUser);
        session.setAttribute("userEmail", currentUser.getEmail());
        
        return "redirect:/profile?success=true";
    }
}