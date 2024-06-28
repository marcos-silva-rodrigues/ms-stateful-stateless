package com.rodrigues.silva.marcos.ms.any_api.core.dto;

public record AnyResponse(
        String status,
        Integer code,
        AuthUserResponse authUser
) {
}
