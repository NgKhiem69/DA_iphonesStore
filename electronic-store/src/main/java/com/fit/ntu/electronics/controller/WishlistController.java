package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    private Long getOrCreateUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            userId = 1L;
            session.setAttribute("userId", userId);
        }
        return userId;
    }

    @GetMapping
    public String viewWishlist(Model model, HttpSession session) {
        Long userId = getOrCreateUserId(session);
        model.addAttribute("wishlistItems", wishlistService.getWishlistByUser(userId));
        return "wishlist";
    }

    @GetMapping("/add/{productId}")
    public String addToWishlist(@PathVariable("productId") Long productId, HttpSession session) {
        Long userId = getOrCreateUserId(session);
        wishlistService.addToWishlist(productId, userId);
        return "redirect:/wishlist";
    }

    @GetMapping("/remove/{id}")
    public String removeFromWishlist(@PathVariable("id") Long id) {
        wishlistService.removeFromWishlist(id);
        return "redirect:/wishlist";
    }
}