package com.fit.ntu.electronics.repository;

import com.fit.ntu.electronics.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUserId(Long userId);
    WishlistItem findByUserIdAndProductId(Long userId, Long productId);
}