package com.ius.student.request;

public record StudentRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
