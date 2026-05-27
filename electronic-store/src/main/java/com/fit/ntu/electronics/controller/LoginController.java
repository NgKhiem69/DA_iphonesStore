package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.model.User;
import com.fit.ntu.electronics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userEmail", user.getEmail());
            
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/orders";
            }
            return "redirect:/";
        }
        model.addAttribute("error", "Email hoặc mật khẩu không chính xác");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                Model model) {
        
        if (userRepository.findByEmail(email) != null) {
            model.addAttribute("error", "Email này đã được sử dụng!");
            return "signup";
        }
        
        String firstName = name;
        String lastName = "";
        if (name.contains(" ")) {
            int lastSpaceIndex = name.lastIndexOf(" ");
            firstName = name.substring(0, lastSpaceIndex);
            lastName = name.substring(lastSpaceIndex + 1);
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setRole("USER");
        
        userRepository.save(newUser);
        
        return "redirect:/login?registered=true";
    }
}