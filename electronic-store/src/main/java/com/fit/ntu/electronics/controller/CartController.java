package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    private Long getOrCreateUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            userId = 1L;
            session.setAttribute("userId", userId);
        }
        return userId;
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId, HttpSession session) {
        Long userId = getOrCreateUserId(session);
        cartService.addToCart(productId, userId);
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Long userId = getOrCreateUserId(session);
        model.addAttribute("cartItems", cartService.getCartItemsByUser(userId));
        return "cart";
    }

    @GetMapping("/remove/{cartItemId}")
    public String removeFromCart(@PathVariable("cartItemId") Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("itemIds") List<Long> itemIds, 
                             @RequestParam("quantities") List<Integer> quantities) {
        if (itemIds != null && quantities != null) {
            for (int i = 0; i < itemIds.size(); i++) {
                cartService.updateQuantity(itemIds.get(i), quantities.get(i));
            }
        }
        return "redirect:/cart";
    }
}