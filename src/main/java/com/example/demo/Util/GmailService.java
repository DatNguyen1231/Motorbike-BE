package com.example.demo.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class GmailService {
    private final JavaMailSender emailSender;
    public GmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }
    public SimpleMailMessage sendGmail(String subject, String body, String email) {
        SimpleMailMessage emailSetting = new SimpleMailMessage();
        emailSetting.setFrom("kcosten101@gmail.com");
        emailSetting.setTo(email);
        emailSetting.setSubject(subject);
        emailSetting.setText(body);
        emailSender.send(emailSetting);
        return emailSetting;
    }
    public void constructEmailWithHTML(String subject, String body, String email) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(body, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setFrom("kcosten101@gmail.com");
        emailSender.send(mimeMessage);
    }

}
