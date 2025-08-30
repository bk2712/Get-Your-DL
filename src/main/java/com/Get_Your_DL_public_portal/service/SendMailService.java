package com.Get_Your_DL_public_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
}
