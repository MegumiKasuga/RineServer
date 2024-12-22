package edu.carole.data;

import java.util.UUID;

public record UserToken(UUID uid, int token) {
    @Override
    public int hashCode() {
        return uid.hashCode() * 31 +
                token * 31;
    }
}
