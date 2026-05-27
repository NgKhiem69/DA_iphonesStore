package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    private final Long MOCK_USER_ID = 1L;

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId) {
        cartService.addToCart(productId, MOCK_USER_ID);
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItemsByUser(MOCK_USER_ID));
        return "cart";
    }
}