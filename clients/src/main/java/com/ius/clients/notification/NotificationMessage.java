package com.ius.clients.notification;

public record NotificationMessage(
        Integer toStudentID,
        String toStudentEmail,
        String sender,
        String message
) {
}
