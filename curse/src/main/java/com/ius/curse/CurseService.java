package com.ius.curse;

import com.ius.curse.request.CurseAddRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CurseService {

    private final CurseRepository curseRepository;

    public List<Curse> getCurses() {
        return curseRepository.findAll();
    }

    public void addCurse(CurseAddRequest request){
        Optional<Curse> curseOptional = curseRepository.findCurseByName(request.curseName());
        if(curseOptional.isPresent()){
            throw new IllegalStateException("Curse called " + request.curseName() + " was already added");
        }

        Curse curse = Curse.builder()
                .name(request.curseName())
                .build();

        curseRepository.save(curse);
    }

    public Optional<Curse> getCurseByName(String curseName) {
        return curseRepository.findCurseByName(curseName);
    }
}
