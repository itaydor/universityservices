package com.ius.student.registaration;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
