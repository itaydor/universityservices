package com.ius.notification;

import com.ius.clients.notification.NotificationMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationMessage notificationMessage){
        notificationRepository.save(
                Notification.builder()
                        .toStudentID(notificationMessage.toStudentID())
                        .toStudentEmail(notificationMessage.toStudentEmail())
                        .sender(notificationMessage.sender())
                        .message(notificationMessage.message())
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}
