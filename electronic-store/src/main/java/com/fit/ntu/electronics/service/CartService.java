package com.fit.ntu.electronics.service;

import com.fit.ntu.electronics.model.CartItem;
import com.fit.ntu.electronics.model.Product;
import com.fit.ntu.electronics.model.User;
import com.fit.ntu.electronics.repository.CartItemRepository;
import com.fit.ntu.electronics.repository.ProductRepository;
import com.fit.ntu.electronics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public void addToCart(Long productId, Long userId) {
        Product product = productRepository.findById(productId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (product != null && user != null) {
            CartItem existingItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                cartItemRepository.save(existingItem);
            } else {
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setUser(user);
                newItem.setQuantity(1);
                cartItemRepository.save(newItem);
            }
        }
    }

    public List<CartItem> getCartItemsByUser(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId).orElse(null);
        if (item != null) {
            if (quantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
    }
}