package com.fit.ntu.electronics.service;

import com.fit.ntu.electronics.model.Product;
import com.fit.ntu.electronics.model.WishlistItem;
import com.fit.ntu.electronics.repository.ProductRepository;
import com.fit.ntu.electronics.repository.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistItemRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addToWishlist(Long productId, Long userId) {
        WishlistItem existingItem = wishlistRepository.findByUserIdAndProductId(userId, productId);
        if (existingItem == null) {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                WishlistItem newItem = new WishlistItem();
                newItem.setUserId(userId);
                newItem.setProduct(product);
                wishlistRepository.save(newItem);
            }
        }
    }

    public List<WishlistItem> getWishlistByUser(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public void removeFromWishlist(Long id) {
        wishlistRepository.deleteById(id);
    }
}