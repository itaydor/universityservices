package com.ius.student.request;

public record StudentCurseRequest(
        Integer studentID,
        String curseName
) {
}
