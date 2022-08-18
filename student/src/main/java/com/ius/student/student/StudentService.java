package com.ius.student.student;

import com.ius.clients.curse.CurseClient;
import com.ius.clients.curse.CurseGetByNameResponse;
import com.ius.clients.notification.NotificationMessage;
import com.ius.clients.registered.RegisteredClient;
import com.ius.clients.registered.RegisteredCurseRequest;
import com.ius.student.exception.ApiRequestException;
import com.ius.student.kafka.KafkaProducer;
import com.ius.student.registaration.token.ConfirmationToken;
import com.ius.student.registaration.token.ConfirmationTokenService;
import com.ius.student.request.StudentCurseRequest;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StudentRepository studentRepository;
    private final CurseClient curseClient;
    private final RegisteredClient registeredClient;
    private final KafkaProducer kafkaProducer;

    private final ConfirmationTokenService confirmationTokenService;

    public void joinCurse(StudentCurseRequest studentCurseRequest) {

        Optional<Student> studentOptional = studentRepository.getStudentByStudentID(studentCurseRequest.studentID());
        validate(studentOptional.isPresent(), "Could not find student with ID:" + studentCurseRequest.studentID());

        CurseGetByNameResponse curseGetByNameResponse = curseClient.getCurseByName(studentCurseRequest.curseName());
        validate(curseGetByNameResponse.isExists(), "Could not find Curse: " + studentCurseRequest.curseName());

        registeredClient.joinCurse(new RegisteredCurseRequest(
                studentCurseRequest.studentID(),
                curseGetByNameResponse.curseID()
        ));

        Student student = studentOptional.get();
        String message = String.format("Hi %s %s, You joined to course %s", student.getFirstName(), student.getLastName(), studentCurseRequest.curseName());
        NotificationMessage notificationMessage = new NotificationMessage(student.getStudentID(), student.getEmail(), "University", message);
        kafkaProducer.send("university", notificationMessage);
    }

    public void leaveCurse(StudentCurseRequest studentCurseRequest) {
        Optional<Student> studentOptional = studentRepository.getStudentByStudentID(studentCurseRequest.studentID());
        validate(studentOptional.isPresent(), "Could not find student with ID:" + studentCurseRequest.studentID());

        CurseGetByNameResponse curseGetByNameResponse = curseClient.getCurseByName(studentCurseRequest.curseName());
        validate(curseGetByNameResponse.isExists(), "Could not find Curse: " + studentCurseRequest.curseName());

        registeredClient.leaveCurse(new RegisteredCurseRequest(
                studentCurseRequest.studentID(),
                curseGetByNameResponse.curseID()
        ));

        Student student = studentOptional.get();
        String message = String.format("Hi %s %s, You have left course %s", student.getFirstName(), student.getLastName(), studentCurseRequest.curseName());
        NotificationMessage notificationMessage = new NotificationMessage(student.getStudentID(), student.getEmail(), "University", message);
        kafkaProducer.send("university", notificationMessage);
    }

    private void validate(boolean exp, String messageIfFalse) {
        if(!exp){
            throw new ApiRequestException(messageIfFalse);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return studentRepository.getStudentByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpStudent(Student student){
        boolean studentExists = studentRepository.getStudentByEmail(student.getEmail()).isPresent();
        if(studentExists){
            throw new ApiRequestException("email is already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(student.getPassword());
        student.setPassword(encodedPassword);

        studentRepository.save(student);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .student(student)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String fullName = student.getFirstName() + " " + student.getLastName();
        String link = "http://localhost:8080/api/v1/students/registration/confirm?token=" + token;
        String message = buildEmail(fullName, link);

        NotificationMessage notificationMessage = new NotificationMessage(
            student.getStudentID(),
            student.getEmail(),
            "University",
                message
        );

        kafkaProducer.send("university", notificationMessage);

        return token;
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public int enableStudent(String email) {
        return studentRepository.enableStudent(email);
    }

    public int setLocked(String email, boolean isLocked){
        return studentRepository.setLocked(email, isLocked);
    }
}
