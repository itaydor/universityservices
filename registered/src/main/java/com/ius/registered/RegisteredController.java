package com.ius.registered;

import com.ius.clients.registered.RegisteredCurseRequest;
import com.ius.registered.request.RegisteredLeaveCurseRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registered")
@Slf4j
@AllArgsConstructor
public class RegisteredController {

    private final RegisteredService registeredService;

    @PostMapping
    public void joinCurse(@RequestBody RegisteredCurseRequest registeredCurseRequest){
        log.info("Join curse request {}", registeredCurseRequest);
        registeredService.joinCurse(
                registeredCurseRequest.studentID(),
                registeredCurseRequest.curseID());
    }

    @DeleteMapping
    public void leaveCurse(@RequestBody RegisteredCurseRequest registeredCurseRequest){
        log.info("Leave curse request {}", registeredCurseRequest);
        registeredService.leaveCurse(new RegisteredCurseRequest(
                registeredCurseRequest.studentID(),
                registeredCurseRequest.curseID()));
    }
}
