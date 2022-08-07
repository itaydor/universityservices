package com.ius.curse;

import com.ius.clients.curse.CurseGetByNameResponse;
import com.ius.curse.request.CurseAddRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/curse")
@Slf4j
@AllArgsConstructor
public class CurseController {

    private final CurseService curseService;

    @GetMapping
    public List<Curse> getCurses(){
        log.info("Get Curses request");
        return curseService.getCurses();
    }

    @PostMapping
    public void addCurse(@RequestBody CurseAddRequest request){
        log.info("New Curse addition {}", request);
        curseService.addCurse(request);
    }

    @GetMapping(path = "{curseName}")
    public CurseGetByNameResponse getCurseByName(@PathVariable("curseName") String curseName){
        log.info("Curse check for {}", curseName);
        Optional<Curse> curseOptional = curseService.getCurseByName(curseName);
        Integer curseID = curseOptional.isPresent() ? curseOptional.get().getCurseID() : 0;
        return new CurseGetByNameResponse(curseOptional.isPresent(), curseID);
    }
}
