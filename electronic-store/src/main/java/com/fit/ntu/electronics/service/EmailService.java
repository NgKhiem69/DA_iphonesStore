package com.fit.ntu.electronics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderEmail(String to, String content) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Xác nhận đơn hàng - Exclusive");
        helper.setText(content, true);
        mailSender.send(message);
    }

    public void sendOtpEmail(String to, String otp) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Mã xác thực khôi phục mật khẩu - Exclusive");
        
        String content = "<div style='font-family: Arial, sans-serif; padding: 20px; text-align: center;'>"
                       + "<h2 style='color: #db4444;'>KHÔI PHỤC MẬT KHẨU</h2>"
                       + "<p>Mã xác thực (OTP) của bạn là:</p>"
                       + "<h1 style='background: #f4f4f4; padding: 10px; letter-spacing: 5px; color: #000;'>" + otp + "</h1>"
                       + "<p>Vui lòng không chia sẻ mã này cho bất kỳ ai. Mã có hiệu lực trong phiên làm việc hiện tại.</p>"
                       + "</div>";
                       
        helper.setText(content, true);
        mailSender.send(message);
    }
}