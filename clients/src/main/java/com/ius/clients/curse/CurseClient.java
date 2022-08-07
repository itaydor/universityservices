package com.ius.clients.curse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("curse")
public interface CurseClient {

    @GetMapping(path = "api/v1/curse/{curseName}")
    CurseGetByNameResponse getCurseByName(@PathVariable("curseName") String curseName);
}
