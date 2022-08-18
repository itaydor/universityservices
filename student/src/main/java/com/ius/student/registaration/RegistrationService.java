package com.ius.student.registaration;

import com.ius.student.exception.ApiRequestException;
import com.ius.student.registaration.token.ConfirmationToken;
import com.ius.student.registaration.token.ConfirmationTokenService;
import com.ius.student.student.Student;
import com.ius.student.student.StudentRule;
import com.ius.student.student.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final static String EMAIL_NOT_FOUND_MSG =
            "email %s not found";
    private final EmailValidator emailValidator;
    private final StudentService studentService;

    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request){

        boolean isValidEmail = emailValidator.test(request.email());

        if(!isValidEmail){
            throw new ApiRequestException(EMAIL_NOT_FOUND_MSG + request.email());
        }

        return studentService.signUpStudent(
                Student.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .password(request.password())
                        .studentRole(StudentRule.USER)
                        .enabled(false)
                        .locked(false)
                        .build()
        );
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
                () -> new ApiRequestException("Token not found")
        );

        if(confirmationToken.getConfirmedAt() != null){
            throw new ApiRequestException("Email already confirmed");
        }

        confirmationTokenService.setConfirmedAt(token);
        studentService.enableStudent(confirmationToken.getStudent().getEmail());

        return "Confirmed";
    }
}
