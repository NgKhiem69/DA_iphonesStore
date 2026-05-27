package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.model.CartItem;
import com.fit.ntu.electronics.model.Order;
import com.fit.ntu.electronics.repository.OrderRepository;
import com.fit.ntu.electronics.service.CartService;
import com.fit.ntu.electronics.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping("/checkout")
    public String checkout(HttpSession session) {
        Long userId = getOrCreateUserId(session);
        List<CartItem> cartItems = cartService.getCartItemsByUser(userId);
        
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }
        
        double total = 0;
        StringBuilder productListHtml = new StringBuilder();
        
        for (CartItem item : cartItems) {
        	double subtotal = item.getProduct().getPrice().doubleValue() * item.getQuantity();
            total += subtotal;
            
            productListHtml.append("<tr>")
                .append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(item.getProduct().getName()).append("</td>")
                .append("<td style='padding: 10px; border: 1px solid #ddd; text-align: center;'>").append(item.getQuantity()).append("</td>")
                .append("<td style='padding: 10px; border: 1px solid #ddd; text-align: right;'>").append(String.format("%,.0f", subtotal)).append(" VNĐ</td>")
                .append("</tr>");
        }
        Order order = new Order();
        order.setAddress("Nha Trang, Khánh Hòa");
        order.setPhone("0123456789");
        order.setTotalPrice(BigDecimal.valueOf(total));
        order.setStatus("Đang chờ duyệt");
        orderRepository.save(order);
        
        String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e5e5e5; padding: 20px;'>"
                + "<h2 style='color: #db4444; text-align: center;'>CẢM ƠN BẠN ĐÃ ĐẶT HÀNG!</h2>"
                + "<p>Chào khách hàng, đơn hàng của bạn đã được tiếp nhận thành công trên hệ thống giả lập đồ án.</p>"
                + "<p><strong>Trạng thái đơn hàng:</strong> <span style='background: #28a745; color: white; padding: 3px 8px; border-radius: 3px;'>Đang chuẩn bị hàng</span></p>"
                + "<table style='width: 100%; border-collapse: collapse; margin-top: 20px;'>"
                + "<thead><tr style='background: #f4f4f4;'> <th style='padding: 10px; border: 1px solid #ddd;'>Sản phẩm</th> <th style='padding: 10px; border: 1px solid #ddd;'>SL</th> <th style='padding: 10px; border: 1px solid #ddd;'>Thành tiền</th> </tr></thead>"
                + "<tbody>" + productListHtml.toString() + "</tbody>"
                + "</table>"
                + "<h3 style='text-align: right; color: #db4444; margin-top: 20px;'>Tổng thanh toán: " + String.format("%,.0f", total) + " VNĐ</h3>"
                + "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>"
                + "<p style='font-size: 12px; color: #777; text-align: center;'>Đây là email thử nghiệm tự động từ hệ thống điện tử NTU.</p>"
                + "</div>";
                
        emailService.sendOrderEmail("gk699488@gmail.com", emailContent);
        
        cartService.clearCart(userId);
        
        return "redirect:/cart?success=true";
    }

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
    @Autowired
    private OrderRepository orderRepository;

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