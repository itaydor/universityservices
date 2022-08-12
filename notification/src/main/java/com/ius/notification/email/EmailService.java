package com.ius.notification.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;

    @Override
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, false);
            helper.setTo(to);
            helper.setSubject("Notification from University");
            helper.setFrom("University@University.ac.il");
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
