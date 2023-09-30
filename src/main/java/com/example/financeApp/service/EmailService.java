package com.example.financeApp.service;

import com.example.financeApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${backend.url}")
    private String backendUrl;

    public void sendConfirmationEmail(User user, String token) {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);
        try {
            helper.setFrom("botrlol@mail.ru");
            helper.setTo(user.getEmail());
            helper.setSubject("Confirm Your Account");
            helper.setText("Please click the link to confirm your account: " + backendUrl + "/api/confirm-email?token=" + token);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mail);
    }
}


