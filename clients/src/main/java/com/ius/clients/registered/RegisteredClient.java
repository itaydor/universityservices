package com.ius.clients.registered;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("registered")
public interface RegisteredClient {

    @PostMapping("api/v1/registered")
    void joinCurse(@RequestBody RegisteredCurseRequest registeredCurseRequest);

    @DeleteMapping("api/v1/registered")
    void leaveCurse(@RequestBody RegisteredCurseRequest registeredCurseRequest);
}
