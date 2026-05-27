package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.service.CategoryService;
import com.fit.ntu.electronics.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String index(@RequestParam(required = false) Long categoryId, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        if (categoryId != null) {
            model.addAttribute("products", productService.getProductsByCategoryId(categoryId));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "index";
    }
}