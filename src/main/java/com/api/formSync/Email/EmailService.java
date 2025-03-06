package com.api.formSync.Email;

import com.api.formSync.exception.EmailSenderFailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM;

    @Async
    public void sendEmailAsync(String to, String subject, String body) {
        sendEmail(to, subject, body);
        System.out.println("\uD83D\uDCE7 Email Sent to: " + to);
    }

    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(FROM);
            helper.setTo(to);
            helper.setSentDate(new Date());
            helper.setSubject(subject);
            helper.setText(body, true);
        } catch (MessagingException e) {
            throw new EmailSenderFailException();
        }
        mailSender.send(message);
    }
}
