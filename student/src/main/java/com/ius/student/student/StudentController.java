package com.ius.student.student;

import com.ius.student.registaration.RegistrationRequest;
import com.ius.student.registaration.RegistrationService;
import com.ius.student.request.StudentCurseRequest;
import com.ius.student.student.Student;
import com.ius.student.student.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@Slf4j
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final RegistrationService registrationService;

    @PostMapping(path = "registration")
    public String register(@RequestBody RegistrationRequest request){
        log.info("New Student registration {}", request);
        return registrationService.register(request);
    }

    @GetMapping(path = "registration/confirm")
    public String confirm(@RequestParam("token") String token){
        log.info("New Student confirmation for token {}", token);
        registrationService.confirmToken(token);
        return "confirmed";
    }

    @PostMapping(path = "curse")
    public void joinCurse(@RequestBody StudentCurseRequest studentCurseRequest){
        log.info("Join curse {}", studentCurseRequest);
        studentService.joinCurse(studentCurseRequest);
    }

    @DeleteMapping(path = "curse")
    public void leaveCurse(@RequestBody StudentCurseRequest studentCurseRequest){
        log.info("leave curse {}", studentCurseRequest);
        studentService.leaveCurse(studentCurseRequest);
    }
}
