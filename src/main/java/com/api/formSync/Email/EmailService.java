package com.api.formSync.Email;

import com.api.formSync.exception.EmailSenderFailException;
import com.api.formSync.util.Log;
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

    @Value("${ENVIRONMENT}")
    private String environment;

    @Async
    public void sendEmailAsync(String to, String subject, String body) {
        sendEmail(to, subject, body);
        Log.blue("Email sent to: ", to);
    }

    public void sendEmail(String to, String subject, String body) {
        if (environment.equals("local")) {
            System.out.println("Email sent from Local Environment");
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(FROM);
            helper.setTo(to);
            helper.setSentDate(new Date());
            helper.setSubject(subject);
            helper.setText(body, true);
            message.addHeader("List-Unsubscribe", "<mailto:unsubscribe@zunaiddev@gmail.com>, <https://formsync.com/unsubscribe>");
        } catch (MessagingException e) {
            throw new EmailSenderFailException("Unable To Send Email.");
        }

        mailSender.send(message);
    }
}
