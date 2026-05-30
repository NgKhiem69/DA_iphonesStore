package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.model.User;
import com.fit.ntu.electronics.model.Order;
import com.fit.ntu.electronics.repository.UserRepository;
import com.fit.ntu.electronics.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository; // Bổ sung OrderRepository

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
        
        // Lấy danh sách đơn hàng của người dùng và đưa vào Model
        List<Order> orders = orderRepository.findByUserId(userId);
        model.addAttribute("orders", orders);
        
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
                // Load lại danh sách đơn hàng nếu có lỗi để tránh giao diện bị lỗi
                model.addAttribute("orders", orderRepository.findByUserId(userId));
                model.addAttribute("user", currentUser);
                model.addAttribute("error", "Mật khẩu hiện tại không chính xác");
                return "profile";
            }
            if (!newPassword.equals(confirmPassword)) {
                // Load lại danh sách đơn hàng nếu có lỗi để tránh giao diện bị lỗi
                model.addAttribute("orders", orderRepository.findByUserId(userId));
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