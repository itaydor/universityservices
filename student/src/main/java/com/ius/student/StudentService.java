package com.ius.student;

import com.ius.clients.curse.CurseClient;
import com.ius.clients.curse.CurseGetByNameResponse;
import com.ius.clients.notification.NotificationMessage;
import com.ius.clients.registered.RegisteredClient;
import com.ius.clients.registered.RegisteredCurseRequest;
import com.ius.student.request.StudentCurseRequest;
import com.ius.student.request.StudentRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final StudentRepository studentRepository;
    private final CurseClient curseClient;
    private final RegisteredClient registeredClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void registerStudent(StudentRegistrationRequest studentRegistrationRequest) {

        Optional<Student> studentOptional = studentRepository.getStudentByEmail(studentRegistrationRequest.email().toLowerCase());

        validate(studentOptional.isEmpty(), "Email is already in use!");

        Student student = Student.builder()
                .firstName(studentRegistrationRequest.firstName())
                .lastName(studentRegistrationRequest.lastName())
                .email(studentRegistrationRequest.email().toLowerCase())
                .password(studentRegistrationRequest.password())
                .build();

        studentRepository.save(student);

        String message = String.format("Hi %s %s, You are now Registered!", student.getFirstName(), student.getLastName());
        NotificationMessage notificationMessage = new NotificationMessage(student.getStudentID(), student.getEmail(), "University", message);
        kafkaTemplate.send("university", notificationMessage);
    }

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
        kafkaTemplate.send("university", notificationMessage);
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
        kafkaTemplate.send("university", notificationMessage);
    }

    public Student getStudentByID(Integer studentID) {
        Optional<Student> studentOptional = studentRepository.getStudentByStudentID(studentID);
        validate(studentOptional.isPresent(), "There is no student with ID: " + studentID);
        return studentOptional.get();
    }

    private void validate(boolean exp, String messageIfFalse) {
        if(!exp){
            throw new IllegalStateException(messageIfFalse);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return studentRepository.getStudentByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String register(StudentRegistrationRequest request) {
        return "it works";
    }
}
