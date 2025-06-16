package com.example.be_deliveryfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    // Sinh OTP 6 số
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Gửi OTP cho đăng ký tài khoản
    public String sendRegisterOtp(String to) {
        String otp = generateOtp();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã xác thực đăng ký tài khoản DeliveryFood");
        message.setText("Mã OTP xác thực đăng ký của bạn là: " + otp);
        mailSender.send(message);
        return otp;
    }

    // Gửi OTP cho quên mật khẩu
    public String sendForgotPasswordOtp(String to) {
        String otp = generateOtp();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã xác thực quên mật khẩu DeliveryFood");
        message.setText("Mã OTP xác thực quên mật khẩu của bạn là: " + otp);
        mailSender.send(message);
        return otp;
    }
}