package com.api.formSync.Email;

import com.api.formSync.dto.FormResponse;
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
    private final EmailTemplate emailTemplate;

    @Value("${spring.mail.username}")
    private String FROM;

    @Value("${ENVIRONMENT}")
    private String environment;

    private void sendEmail(String to, String subject, String body) {
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
            throw new RuntimeException("Unable To Send Email.");
        }

        mailSender.send(message);
    }

    public void sendVerificationEmail(String to, String name, String token) {
        sendEmail(to, "Please verify your email", emailTemplate.verificationBody(name, token));
    }

    public void sendPasswordResetEmail(String to, String name, String token) {
        sendEmail(to, "Password Reset Request", emailTemplate.passwordResetBody(name, token));
    }

    public void sendEmailChangeConfirmationEmail(String to, String name, String token) {
        sendEmail(to, "Confirm Your New Email Address", emailTemplate.emailChangeBody(name, token));
    }

    @Async
    public void sendFormEmail(String to, FormResponse response) {
        sendEmail(to, "New Form Submission Received", emailTemplate.formSubmissionBody(response));
    }
}