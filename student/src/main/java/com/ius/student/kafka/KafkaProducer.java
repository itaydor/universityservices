package com.ius.student.kafka;

import com.ius.clients.notification.NotificationMessage;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, NotificationMessage message){
        kafkaTemplate.send(topic, message);
    }
}
