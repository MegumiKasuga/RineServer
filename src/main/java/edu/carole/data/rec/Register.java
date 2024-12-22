package edu.carole.data.rec;

import lombok.Getter;

import java.util.UUID;

public class Register extends Login {

    @Getter
    private final String name;

    public Register(UUID uuid, int token, String name) {
        super(uuid, token);
        this.name = name;
    }
}
