package com.fit.ntu.electronics.controller;

import com.fit.ntu.electronics.model.Order;
import com.fit.ntu.electronics.repository.OrderRepository;
import com.fit.ntu.electronics.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "admin/order-list";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("orderId") Long orderId, @RequestParam("status") String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);

            String recipientEmail = "gk699488@gmail.com";
            
            if (order.getUser() != null && order.getUser().getEmail() != null) {
                recipientEmail = order.getUser().getEmail();
            }

            String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e5e5e5; padding: 20px;'>"
                    + "<h2 style='color: #db4444; text-align: center;'>CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG</h2>"
                    + "<p>Mã đơn hàng: <strong>#" + order.getId() + "</strong></p>"
                    + "<p>Trạng thái mới: <span style='background: #007bff; color: white; padding: 3px 8px; border-radius: 3px;'>" + status + "</span></p>"
                    + "<p>Hệ thống điện tử đang xử lý kiện hàng của bạn.</p>"
                    + "</div>";

            try {
                emailService.sendOrderEmail(recipientEmail, emailContent);
            } catch (Exception e) {
                // In lỗi ra console để debug thay vì làm sập trang web
                System.err.println("Lỗi gửi email cho đơn hàng " + orderId + ": " + e.getMessage());
            }
        }
        return "redirect:/admin/orders";
    }
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", order);
        return "admin/order-detail";
    }
}