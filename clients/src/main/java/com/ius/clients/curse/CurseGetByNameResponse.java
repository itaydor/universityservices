package com.ius.clients.curse;

public record CurseGetByNameResponse(
        Boolean isExists,
        Integer curseID
) {
}
