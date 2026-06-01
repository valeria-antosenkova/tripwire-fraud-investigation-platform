package com.teamcrocodile.tripwire.controller.dto;

public record CreateAgentRequest(
        int adminId,
        String name,
        String email,
        String password
) {
}
