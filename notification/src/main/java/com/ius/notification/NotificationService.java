package com.ius.notification;

import com.ius.clients.notification.NotificationMessage;
import com.ius.notification.email.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailSender emailSender;

    public void send(NotificationMessage notificationMessage){
        notificationRepository.save(
                Notification.builder()
                        .toStudentID(notificationMessage.toStudentID())
                        .toStudentEmail(notificationMessage.toStudentEmail())
                        .sender(notificationMessage.sender())
                        .message("Confirmation email was sent")
                        .sentAt(LocalDateTime.now())
                        .build()
        );

        emailSender.send(notificationMessage.toStudentEmail(), notificationMessage.message());
    }
}
