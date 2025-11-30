package com.Get_Your_DL_public_portal.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Async
public class SendMailService {
    @Autowired
    JavaMailSender javaMailSender;

    private final String sender= "kaushikbhavesh20@gmail.com";

    @Async
    public void sendEmailToUsers(String subject, String body, String to){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(sender);
        javaMailSender.send(message);

    }

    @Async
    public void sendHtmlEmailToUsers(String subject, String body, String to){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = send as HTML
            helper.setFrom(sender);
            FileSystemResource img = new FileSystemResource(new File("src/main/resources/static/DL Registration Header.png"));
            helper.addInline("headerImage", img);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
