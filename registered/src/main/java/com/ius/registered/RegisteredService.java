package com.ius.registered;

import com.ius.clients.registered.RegisteredCurseRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisteredService {

    private final RegisteredRepository registeredRepository;

    public void joinCurse(Integer studentID, Integer courseID){
        Optional<Registered> optionalRegistered =
                registeredRepository.getRegisteredByStudentIDAndCurseID(studentID, courseID);
        if(optionalRegistered.isPresent()){
            throw new IllegalStateException("Student is already registered to this curse!");
        }

        Registered registered = Registered.builder()
                .studentID(studentID)
                .curseID(courseID)
                .build();

        registeredRepository.save(registered);
    }

    public void leaveCurse(RegisteredCurseRequest request){
        Optional<Registered> optionalRegistered = registeredRepository.getRegisteredByStudentIDAndCurseID(request.studentID(), request.curseID());
        if(optionalRegistered.isEmpty()){
            throw new IllegalStateException("Student is not registered to this curse! " + request);
        }
        registeredRepository.deleteById(optionalRegistered.get().getId());
    }
}
