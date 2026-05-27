package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.model.User;
import com.fit.ntu.electronics.repository.UserRepository;
import com.fit.ntu.electronics.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Email này chưa được đăng ký!");
            return "forgot-password";
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        session.setAttribute("resetEmail", email);
        session.setAttribute("resetOtp", otp);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi mạng khi gửi email, vui lòng thử lại sau.");
            return "forgot-password";
        }

        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(HttpSession session) {
        if (session.getAttribute("resetEmail") == null) {
            return "redirect:/forgot-password";
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("otp") String otp,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       HttpSession session, Model model) {
                                           
        String sessionOtp = (String) session.getAttribute("resetOtp");
        String email = (String) session.getAttribute("resetEmail");

        if (sessionOtp == null || !sessionOtp.equals(otp)) {
            model.addAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn.");
            return "reset-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới không trùng khớp.");
            return "reset-password";
        }

        User user = userRepository.findByEmail(email);
        user.setPassword(newPassword);
        userRepository.save(user);

        session.removeAttribute("resetOtp");
        session.removeAttribute("resetEmail");

        return "redirect:/login?resetSuccess=true";
    }
}