package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.model.Product;
import com.fit.ntu.electronics.model.Category;
import com.fit.ntu.electronics.repository.ProductRepository;
import com.fit.ntu.electronics.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String index(@RequestParam(value = "categoryId", required = false) Long categoryId,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        Model model) {
        
        List<Category> categories = categoryRepository.findAll();
        
        Pageable pageable = PageRequest.of(page, 16);
        Page<Product> productPage;

        if (categoryId != null) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        model.addAttribute("categories", categories);
        model.addAttribute("productPage", productPage);
        model.addAttribute("selectedCategory", categoryId);
        
        return "index";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        
        if (optionalProduct.isEmpty()) {
            return "redirect:/";
        }
        
        Product product = optionalProduct.get();
        model.addAttribute("product", product);
        
        if (product.getCategory() != null) {
            Pageable pageable = PageRequest.of(0, 4);
            Page<Product> relatedProducts = productRepository.findByCategoryIdAndIdNot(
                    product.getCategory().getId(), id, pageable);
            model.addAttribute("relatedProducts", relatedProducts.getContent());
        } else {
            model.addAttribute("relatedProducts", java.util.List.of());
        }
        
        return "product-detail";
    }
}