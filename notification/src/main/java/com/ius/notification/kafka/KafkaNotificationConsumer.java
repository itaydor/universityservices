package com.ius.notification.kafka;

import com.ius.clients.notification.NotificationMessage;
import com.ius.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "university",
            groupId = "group_id",
            containerFactory = "notificationRequestFactory"
    )
    public void consume(NotificationMessage notificationMessage){
        log.info("Pull message from queue: {}", notificationMessage);
        notificationService.send(notificationMessage);
    }
}
