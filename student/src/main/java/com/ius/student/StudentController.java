package com.ius.student;

import com.ius.student.request.StudentCurseRequest;
import com.ius.student.request.StudentRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@Slf4j
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping(path = "registration")
    public String register(@RequestBody StudentRegistrationRequest request){
        return studentService.register(request);
    }

    @GetMapping(path = "{studentID}")
    public Student getStudentByID(@PathVariable("studentID") Integer studentID){
        log.info("Get Student by ID {}", studentID);
        return studentService.getStudentByID(studentID);
    }

    @PostMapping
    public void registerStudent(@RequestBody StudentRegistrationRequest request){
        log.info("New Student registration {}", request);
        studentService.registerStudent(request);
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
