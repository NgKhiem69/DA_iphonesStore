package com.fit.ntu.electronics.service;

import com.fit.ntu.electronics.model.Product;
import com.fit.ntu.electronics.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        return optional.orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public Page<Product> getRelatedProducts(Long categoryId, Long productId, Pageable pageable) {
        return productRepository.findByCategoryIdAndIdNot(categoryId, productId, pageable);
    }
}